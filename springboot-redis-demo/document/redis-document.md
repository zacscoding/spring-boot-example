# redis

## Index  
- <a href="#install"> install </a>
- <a href="#configuration"> configuration </a>
- <a href="#data-type"> data type </a>
- <a href="#commands"> commands </a>
- <a href="#keys"> keys </a>

## References
- https://redis.io/
- http://dgkim5360.tistory.com/entry/install-redis-for-linux-or-windows
- https://www.tutorialspoint.com/redis/index.htm


<div id="install"></div>

## Install  


> 다운로드 & 압축 해제

```
$ wget http://download.redis.io/redis-stable.tar.gz
$ cd redis-stable
$ make
$ sudo make install
```

> run & cli

```
$ redis-server
$ redis-cli
```

> alias

```
# alias redis
alias redis_start='redis-server /usr/local/redis/redis-stable/redis.conf'
alias redis_stop='redis-cli -h 127.0.0.1 -p 6379 -a foobared shutdown'
```



<div id="configuration"></div>

---

## Configuration  

> CONFIG GET CONFIG_SETTING_NAME

```
127.0.0.1:6379> config get loglevel
1) "loglevel"
2) "notice"
```

> CONFIG GET *

```
127.0.0.1:6379> config get *
  1) "dbfilename"
  2) "dump.rdb"
  3) "requirepass"
  4) ""
  5) "masterauth"
  ...
```

> CONFIG SET CONFIG_SETTING_NAME NEW_CONFIG_VALUE  

```
127.0.0.1:6379> config set loglevel "notice"
OK
127.0.0.1:6379> CONFIG GET loglevel
1) "loglevel"
2) "notice"
```

> disk에 저장하지 않기

```
127.0.0.1:6379> config get save
1) "save"
2) "3600 1 300 100 60 10000"
127.0.0.1:6379> config set save ""
OK
127.0.0.1:6379> config get save
1) "save"
2) ""
```

---

<div id="data-type"></div>

## Data Types

#### Strings
- 레디스는 일련의 바이트  
- 바이너리 safe ?  
=> they have a known length not determined by any special terminating characters  
- 하나의 스트링에 512 mb 까지 저장 가능  
- https://redis.io/commands#string

> Eg) SET name "zaccoding"

> redis-cli

```
127.0.0.1:6379> set name zaccoding
OK
127.0.0.1:6379> get name
"zaccoding"
```

> Java code

```
  @Resource(name = "redisTemplate")
  private ValueOperations<String, String> valueOperations;

  @Test
  public void setAndGet() {
      valueOperations.set("name", "zaccoding");
      assertThat(valueOperations.get("name"),is("zaccoding"));
  }
```

#### Hashes
- 레디스 Hash는 {key,value}의 컬렉션
- Redis hash는 문자열 필드와 값의 Map  
=> objects를 나타낼 때 사용  
- https://redis.io/commands/hdel

> Eg) HMSET KEY_NAME FIELD1 VALUE1 ... FIELDN VALUEN

> redis-cli

```
127.0.0.1:6379> HMSET user name "zac" hobby "coding"
OK
127.0.0.1:6379> HGET user name
"zac"
127.0.0.1:6379> HGETALL user
1) "name"
2) "zac"
3) "hobby"
4) "coding"
```

> java code  

```
@Resource(name = "redisTemplate")
private HashOperations<String, String, String> hashOperations;

@Test
public void test() {
    Map<String,String> map = new HashMap<>();
    map.put("name", "zac");
    map.put("hobby", "codding");
    hashOperations.putAll("user", map);
    hashOperations.entries("user").forEach((k,v) -> {
        CustomPrinter.println("key : {}, value : {}", k ,v);
    });
}
```

---

#### Lists  
- Redis lists는 삽입 순서에 따라 정렬 된 문자열 리스트
- 리스트의 양끝에 넣을 수 있음(double-ended queue)
- LPUSH , RPUSH , LRANGE
- https://redis.io/commands#list


> Example

> redis-cli

```
127.0.0.1:6379> lpush zaccoding coding1
(integer) 1
127.0.0.1:6379> lpush zaccoding coding2
(integer) 2
127.0.0.1:6379> lpush zaccoding coding3
(integer) 3
127.0.0.1:6379> lpush zaccoding codding4
(integer) 4
127.0.0.1:6379> lrange zaccoding 0 10
1) "codding4"
2) "coding3"
3) "coding2"
4) "coding1"
```

> java code   

```
@Resource(name = "redisTemplate")
private ListOperations<String, String> listOperations;

@Test
public void test() {
    listOperations.rightPush("zac", "coding1");
    listOperations.rightPush("zac", "coding2");
    listOperations.rightPush("zac", "coding3");
    listOperations.leftPush("zac", "coding0");
    listOperations.range("zac", 0 , -1).forEach(v -> {
        CustomPrinter.println("value : " + v);
    });
}
```

```
127.0.0.1:6379> rpush bulk 1 2 3 4 5 "foo bar"
(integer) 6
127.0.0.1:6379> lrange bulk -2 -1
1) "5"
2) "foo bar"
127.0.0.1:6379> lrange bulk 0 -1
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
6) "foo bar"
```

> java example  

```
@Resource(name = "redisTemplate")
private ListOperations<String, String> listOperations;

@Test
public void testBulk() {
    System.out.println(listOperations.rightPushAll("zac", "codding1", "coding2"));
    //assertTrue(listOperations.rightPush("zac", "codding1", "coding2") == 2L);
    listOperations.range("zac", 0 , -1).forEach(v -> {
        CustomPrinter.println("value : " + v);
    });
}
```

#### Sets  
- Redis Sets는 정렬되지 않은 문자열 컬렉션
- Redis 에서는 O(1)의 추가, 제거, 테스트? 가능?  
Redis Sets are an unordered collection of strings. In Redis, you can add, remove, and test for the existence of members in O(1) time complexity.
- Sets의 member의 최대 수는 2^32-1 (4294967295)
- https://redis.io/commands#set

> redis-cli

```
127.0.0.1:6379> sadd zaccoding redis
(integer) 1
127.0.0.1:6379> sadd zaccoding mongodb
(integer) 1
127.0.0.1:6379> sadd zaccoding reditmq
(integer) 1
127.0.0.1:6379> sadd zaccoding reditmq
(integer) 0
127.0.0.1:6379> smembers zaccoding
1) "reditmq"
2) "mongodb"
3) "redis"
```

> java code

```
@Resource(name = "redisTemplate")
private SetOperations<String, String> setOperations;

@Test
public void setUp() {
    assertTrue(setOperations.add("zaccoding", "redis", "mongodb", "reditmq", "reditmq") == 3L);
    Set<String> set = setOperations.members("zaccoding");
    System.out.println("## set class : " + set.getClass().getName());
    assertTrue(set.size() == 3);
}
```

#### Sorted Sets

- Redis Sets와 유사, 중복되지 않는 문자열
- Sorted Set의 Member 들은 정렬을 위해 스코어와 연관이 있음
- member는 unique 하는 반면에, 스코어는 repeated 될 수 있음?

>redis-cli

```
127.0.0.1:6379> zadd zaccoding 0 redis
(integer) 1
127.0.0.1:6379> zadd zaccoding 0 mongodb
(integer) 1
127.0.0.1:6379> zadd zaccoding 0 rabitmq
(integer) 1
127.0.0.1:6379> zadd zaccoding 0 rabitmq
(integer) 0
127.0.0.1:6379> zrangebyscore zaccoding 0 1000
1) "mongodb"
2) "rabitmq"
3) "redis"
```

> java code

```
@Resource(name = "redisTemplate")
private ZSetOperations<String, String> zSetOperations;

@Test
public void test() {
    String key = "zaccoding";
    zSetOperations.add(key, "redis", 0);
    zSetOperations.add(key, "mongodb", 0);
    zSetOperations.add(key, "reditmq", 0);
    zSetOperations.add(key, "reditmq", 0);

    Set<String> result = zSetOperations.rangeByScore(key, 0 , 1000);
    Iterator<String> itr = result.iterator();
    while(itr.hasNext()) {
        System.out.println(itr.next());
    }
}
```

---

<div id="commands"></div>

#### commands

> redis-cli

```
[root@localhost ~]# redis-cli
127.0.0.1:6379> ping
PONG
```

> redis-cli -h host -p port -a password

```
[root@localhost ~]# redis-cli -h 127.0.0.1 -p 6379 -a "zaccoding"
127.0.0.1:6379> ping
PONG
```

---

<div id="keys"> </div>

#### keys

keys command는 Redis의 keys를 관리하는데 사용

> COMMAND KEY_NAME  

```
127.0.0.1:6379> SET zaccoding redis
OK
127.0.0.1:6379> DEL zaccoding
(integer) 1
127.0.0.1:6379> DEL zaccoding
(integer) 0
```

```
127.0.0.1:6379> lpush zaccoding coding1
(integer) 1
127.0.0.1:6379> lrange zaccoding 0 -1
1) "coding1"
127.0.0.1:6379> del zaccoding
(integer) 1
127.0.0.1:6379> lrange zaccoding 0 -1
(empty list or set)
```

<table>
  <tr>
    <td>S.No</td>
    <td>
      Command & Description
    </td>
  </tr>
  <tr>
    <td>DEL KEY</td>
    <td>
      KEY가 존재하면 삭제
    </td>
  </tr>
  <tr>
    <td>DUMP KEY</td>
    <td>
      특정 키에 저장 된 값의 serialized를 반환?
```
127.0.0.1:6379> lpush zaccoding coding2
(integer) 2
127.0.0.1:6379> DUMP zaccoding
"\x0e\x01\x1d\x1d\x00\x00\x00\x13\x00\x00\x00\x02\x00\x00\acoding2\t\acoding1\xff\b\x00\xe6R_\xd6ED\x11\xed"
```
    </td>
  </tr>
  <tr>
    <td>EXISTS KEY</td>
    <td>
      키 존재 여부
```
127.0.0.1:6379> exists zaccoding
(integer) 1
```      
    </td>
  </tr>
  <tr>
    <td>EXPIRE KEY</td>
    <td>
      특정 시간 후에 키의 expiry setting  
```
127.0.0.1:6379> set zac "coding"
OK
127.0.0.1:6379> expire zac 10
(integer) 1
127.0.0.1:6379> TTL zac
(integer) 8
127.0.0.1:6379> exists zac
(integer) 1
127.0.0.1:6379> TTL zac
(integer) 5
127.0.0.1:6379> TTL zac
(integer) 0
127.0.0.1:6379> TTL zac
(integer) -2
127.0.0.1:6379> TTL zac
(integer) -2
127.0.0.1:6379> exists zac
(integer) 0
```      
    </td>
  </tr>
  <tr>
    <td>EXPIREAT key timestamp</td>
    <td>
      특정 시간 후에 키를 만료 => Unix timestamp format을 따름
    </td>
  </tr>
  <tr>
    <td>PEXPIRE key milliseconds</td>
    <td>
      key의 expiry를 밀리세컨드로 setting
```
127.0.0.1:6379> set zac "coding"
OK
127.0.0.1:6379> exists zac
(integer) 1
127.0.0.1:6379> pexpire zac 2000
(integer) 1
127.0.0.1:6379> exists zac
(integer) 1
127.0.0.1:6379> exists zac
(integer) 0
```      
    </td>
  </tr>
  <tr>
    <td>PEXPIREAT key milliseconds-timestamp</td>
    <td>
      Sets the expiry of the key in Unix timestamp specified as milliseconds.
    </td>
  </tr>
  <tr>
    <td>keys pattern</td>
    <td>
    패턴에 매칭되는 키 반환 https://redis.io/commands/keys      
```
127.0.0.1:6379> MSET firstname zac lastname codding age 19
OK
127.0.0.1:6379> keys *name*
1) "firstname"
2) "lastname"
127.0.0.1:6379> keys a??
1) "age"
127.0.0.1:6379> keys *
1) "age"
2) "firstname"
3) "lastname"
```
    </td>
  </tr>
  <tr>
    <td></td>
    <td>
    </td>
  </tr>
  <tr>
    <td></td>
    <td>
    </td>
  </tr>
</table>





















<br /><br /><br /><br /><br /><br /><br /><br /><br />
<br /><br /><br /><br /><br /><br /><br /><br /><br />
