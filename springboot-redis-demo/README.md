# redis

## Index  
- <a href="#install"> install </a>
- <a href="#configuration"> configuration </a>
- <a href="#data-type"> data type </a>
- <a href="#commands"> commands </a>
- <a href="#keys"> keys </a>
- <a href="#strings">Strings</a>
- <a href="#hashes">Hashes</a>

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
127.0.0.1:6379> auth foobared
OK
127.0.0.1:6379> ping
pong
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

- DEL KEY  
=> KEY가 존재하면 삭제
- DUMP KEY  
=> 특정 키에 저장 된 값의 serialized를 반환?  
```
127.0.0.1:6379> lpush zaccoding coding2
(integer) 2
127.0.0.1:6379> DUMP zaccoding
"\x0e\x01\x1d\x1d\x00\x00\x00\x13\x00\x00\x00\x02\x00\x00\acoding2\t\acoding1\xff\b\x00\xe6R_\xd6ED\x11\xed"
```
- EXISTS KEY  
=> 키 존재 여부  

```
127.0.0.1:6379> exists zaccoding
(integer) 1
```      
- EXPIRE KEY  
=> 특정 시간 후에 키의 expiry setting   

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
- EXPIREAT key timestamp  
=> 특정 시간 후에 키를 만료 => Unix timestamp format을 따름
- PEXPIRE key milliseconds  
=> key의 expiry를 밀리세컨드로 setting  

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
- PEXPIREAT key milliseconds-timestamp  
=> Sets the expiry of the key in Unix timestamp specified as milliseconds.  
- keys pattern  
=> 패턴에 매칭되는 키 반환 https://redis.io/commands/keys  

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
- MOVE key db  
=> 키를 다른 데이터베이스로 이동  
- PERSIST key  
=> 키의 expiration을 제거  

```
127.0.0.1:6379> set zac "coding"
OK
127.0.0.1:6379> TTL zac
(integer) -1
127.0.0.1:6379> expire zac 20
(integer) 1
127.0.0.1:6379> TTL zac
(integer) 18
127.0.0.1:6379> PERSIST zac
(integer) 1
127.0.0.1:6379> TTL zac
(integer) -1
```  
- PTTL key  
=> key의 만료 시간(milliseconds)의 남은 시간을 가져옴  

```
127.0.0.1:6379> expire zac 60
(integer) 1
127.0.0.1:6379> TTL zac
(integer) 57
127.0.0.1:6379> PTTL zac
(integer) 53432
```   
- TTL key  
=> 키의 만료 시간을 가져옴  
- RANDOMKEY  
=> 레디스로부터 랜덤 키를 반환
- RENAME key newkey  
=>키 이름 변경  

```
127.0.0.1:6379> set zac "coding"
OK
127.0.0.1:6379> get zac
"coding"
127.0.0.1:6379> RENAME zac zaccoding
OK
127.0.0.1:6379> get zac
(nil)
127.0.0.1:6379> get zaccoding
"coding"
```  
- RENAMENX key newkey  
=> 새로운 키가 존재하지 않으면, 키 이름 변경  

```
127.0.0.1:6379> set zac1 "coding1"
OK
127.0.0.1:6379> set zac2 "coding2"
OK
127.0.0.1:6379> RENAME zac1 zac2
OK
127.0.0.1:6379> get zac2
"coding1"
127.0.0.1:6379> get zac1
(nil)
127.0.0.1:6379> flushall
OK
127.0.0.1:6379> set zac1 "coding1"
OK
127.0.0.1:6379> set zac2 "coding2"
OK
127.0.0.1:6379> RENAMENX zac1 zac2
(integer) 0
127.0.0.1:6379> get zac2
"coding2"
```  
- TYPE key  
=> 키에 저장 된 데이터 타입 반환

---

<div id="strings"></div>

## Strings  

> 문법  

```
> COMMAND KEY_NAME  
```

> Ex  

```
127.0.0.1:6379> set zac coding
OK
127.0.0.1:6379> get zac
"coding"
```

> Strings Commands  

- SET key value  
=> 특정 key에 value를 저장
- GET key  
=> 키의 값을 가져옴
- GETRANGE key start end  
=> 키에 저장 된 string의 substring을 리턴   

```
127.0.0.1:6379> set zac coding
OK
127.0.0.1:6379> GETRANGE zac 0 1
"co"
127.0.0.1:6379> GETRANGE zac 2 -1
"ding"
```   

- GETSET key value
=> 키에 새로운 value를 set & 이전 값을 반환  
```
127.0.0.1:6379> set zac coding
OK
127.0.0.1:6379> GETSET zac newcoding
"coding"
127.0.0.1:6379> get zac
"newcoding"
```      

- GETBIT key offset  
=> 키에 저장 된 문자열에서 offset의 bit 값을 반환      
- MGET key1 [key2..]  
=> 주어진 키의 모든 value를 반환  

```
127.0.0.1:6379> set zac1 coding1
OK
127.0.0.1:6379> set zac2 coding2
OK

127.0.0.1:6379> set zac3 coding3
OK
127.0.0.1:6379> set zac4 coding4
OK
127.0.0.1:6379> MGET zac1 zac2 zac3
1) "coding1"
2) "coding2"
3) "coding3"
```    
- SETBIT key offset value  
=> key에 저장 된 string value의 offset에 set or clear  
- SETEX key seconds value  
=> key의 expire를 가진 value 저장  

```
127.0.0.1:6379> SETEX zac 10 coding
OK
127.0.0.1:6379> TTL zac
(integer) 8
...
127.0.0.1:6379> TTL zac
(integer) 0
127.0.0.1:6379> TTL zac
(integer) -2
127.0.0.1:6379> get zac
(nil)
```    
- SETNX key value  
=> 키가 존재하지 않으면 키의 value를 set  

```
127.0.0.1:6379> set zac coding1
OK
127.0.0.1:6379> setnx zac coding2
(integer) 0
127.0.0.1:6379> get zac
"coding1"
```    
- SETRANGE key offset value  
=> offset부터 덮어씌움  

```
127.0.0.1:6379> set zac abcdefg
OK
127.0.0.1:6379> setrange zac 2 ooo
(integer) 7
127.0.0.1:6379>
127.0.0.1:6379> get zac
"abooofg"
127.0.0.1:6379> setrange zac -1 ooo
(error) ERR offset is out of range
127.0.0.1:6379> setrange zac 6 oo
(integer) 8
127.0.0.1:6379> get zac
"abcdefoo"
127.0.0.1:6379> setrange zac 8 aa
(integer) 10
127.0.0.1:6379> get zac
"abcdefooaa"
127.0.0.1:6379> setrange zac 11 bb
(integer) 13
127.0.0.1:6379> get zac
"abcdefooaa\x00bb"
```    
- STRLEN key  
=> 키에 저장 된 값의 길이를 반환  

```
127.0.0.1:6379> set zac coding
OK
127.0.0.1:6379> strlen zac
(integer) 6
```    
- MSET key value [key value ...]  
=> 다중 [키,값]을 저장  

```
127.0.0.1:6379> MSET zac1 coding1 zac2 coding2 zac3 coding3
OK
127.0.0.1:6379> get zac
(nil)
127.0.0.1:6379> get zac1
"coding1"
127.0.0.1:6379> get zac2
"coding2"
127.0.0.1:6379> get zac3
"coding3"
127.0.0.1:6379> keys zac*
1) "zac2"
2) "zac3"
3) "zac1"
```      
- MSETNX key value [key value ...]  
=> 키가 존재하지 않으면 다중 키,값 저장(하나라도 존재하면 저장X)  

```
127.0.0.1:6379> set zac1 coding1
OK
127.0.0.1:6379> MSETNX zac1 coding1 zac2 coding2 zac3 coding3
(integer) 0
127.0.0.1:6379> keys zac*
1) "zac1"
127.0.0.1:6379> MSETNX zac2 coding2 zac3 coding3 zac1 coding1
(integer) 0
127.0.0.1:6379> keys zac*
1) "zac1"
```      
- PSETEX key milliseconds value  
=> 키 + 값 + expiry 저장  

```
127.0.0.1:6379> PSETEX zac1 5000 coding1
OK
127.0.0.1:6379> get zac1
"coding1"
127.0.0.1:6379> ttl zac1
(integer) 2
127.0.0.1:6379> get zac1
(nil)
127.0.0.1:6379> ttl zac1
(integer) -2
```      
- INCR key  
=> 키의 int value 값을 한단계 증가  

```
127.0.0.1:6379> set zac 1
OK
127.0.0.1:6379> INCR zac
(integer) 2
127.0.0.1:6379> get zac
"2"
127.0.0.1:6379> set zac2 -1
OK
127.0.0.1:6379> get zac2
"-1"
127.0.0.1:6379> INCR zac2
(integer) 0
```      
- INCRBY key increment  
=> 주어진 수만큼 key의 value 값 증가  

```
127.0.0.1:6379> set zac 1
OK
127.0.0.1:6379> INCRBY zac 10
(integer) 11
127.0.0.1:6379> get zac
"11"
127.0.0.1:6379> INCRBY zac -10
(integer) 1
127.0.0.1:6379> get zac
"1"
```    
- INCRBYFLOAT key increment  
=> 주어진 수(float)만큼 key의 value 증가  

```
127.0.0.1:6379> set zac 0.1
OK
127.0.0.1:6379> INCRBYFLOAT zac 1.5
"1.6"
127.0.0.1:6379> get zac
"1.6"
```      
- DECR key  // DECRBY key decrement  
=> 키의 int value 1감소 // decrement 만큼 감소  

```
127.0.0.1:6379> set zac 10
OK
127.0.0.1:6379> DECR zac
(integer) 9
127.0.0.1:6379> DECRBY zac 5
(integer) 4
127.0.0.1:6379> get zac
"4"
```      
- APPEND key value  
=> 키의 value에 appned      
```
127.0.0.1:6379> set zac zac
OK
127.0.0.1:6379> append zac codingspring
(integer) 9
127.0.0.1:6379> get zac
"zaccoding"
```      

[Spring-Data-Redis test  code](https://github.com/zacscoding/spring-boot-example/blob/master/springboot-redis-demo/src/test/java/org/zaccoding/datatypes/StringsTest.java)


---

<div id="hashes"></div>  

## Hashes  
; Redis Hashes는 string fields와 string values의 맵 형태  
=> 약 40억 개 이상의 필드-값을 저장  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding" age 19
OK
127.0.0.1:6379> HGETALL zac
1) "name"
2) "zaccoding"
3) "hobby"
4) "coding"
5) "age"
6) "19"
```  
=> "zac" 이름의 hash에 (name, hobby, age)의 맵 형태 저장  

**Redis Hash Commands**  

> HDEL key field2 [field2]  
=> 하나 이상의 hash fields를 삭제  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding"
OK
127.0.0.1:6379> HGETALL zac
1) "name"
2) "zaccoding"
3) "hobby"
4) "coding"
127.0.0.1:6379> HDEL zac hobby
(integer) 1
127.0.0.1:6379> HGETALL zac
1) "name"
2) "zaccoding"
```  

> HEXISTS key field  
=> a hash field가 존재하는지 체크  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding"
OK
127.0.0.1:6379> HEXISTS zac hobby
(integer) 1
127.0.0.1:6379> HEXISTS zac hobby2
```  

> HGET key field  
=> a hash field에 저장 된 특정 키의 값을 가져옴  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding"
OK
127.0.0.1:6379> HGET zac hobby
"coding"
127.0.0.1:6379> HGET zac hobby2
(nil)
```  

> HINCRBY key field increment  
=> a hash field의 정수 값을 지정 된 수만큼 증가  

```
127.0.0.1:6379> HMSET zac name "zaccoding" age 19
OK
127.0.0.1:6379> HINCRBY zac age 11
(integer) 30
127.0.0.1:6379> HGET zac age
"30"
```  

> HINCRBYFLOAT key field increment  
=> a hash field의 float 값을 지정 된 수만큼 증가  

```
127.0.0.1:6379> HMSET zac name "zaccoding" score 0.123
OK
127.0.0.1:6379> HINCRBYFLOAT zac score 0.5
"0.623"
127.0.0.1:6379> HGET zac score
"0.623"
```  

> HKEYS key  
=> hash안의 모든 fiels를 가져옴  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding" age 19 score 0.95
OK
127.0.0.1:6379> HKEYS zac
1) "name"
2) "hobby"
3) "age"
4) "score"
```  

> HLEN key  
=> hash에 있는 필드의 수를 가져옴  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding" age 19 score 0.95
OK
127.0.0.1:6379> HLEN zac
(integer) 4
```  

> HMGET key field1 [field2]  
=> hash fiels의 값을 가져옴  

```  
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding" age 19 score 0.95
OK
127.0.0.1:6379> HMGET zac name hobby age
1) "zaccoding"
2) "coding"
3) "19"
```  

> HMSET key field1 value1 [field2 value2 ]  
=> 다중 hash fiels의 값들 setting  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding"
OK
127.0.0.1:6379> HMSET zac name "zacscoding"
OK
127.0.0.1:6379> HGETALL zac
1) "name"
2) "zacscoding"
3) "hobby"
4) "coding"
127.0.0.1:6379> HMSET zac hobby "code" age 19
OK
127.0.0.1:6379> HGETALL zac
1) "name"
2) "zacscoding"
3) "hobby"
4) "code"
5) "age"
6) "19"
```  

> HSET key field value  
=> hash fields의 string value를 setting  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding"
OK
127.0.0.1:6379> HSET zac hobby "code"
(integer) 0
127.0.0.1:6379> HGETALL zac
1) "name"
2) "zaccoding"
3) "hobby"
4) "code"
127.0.0.1:6379> HSET zac major "math"
(integer) 1
127.0.0.1:6379> HGETALL zac
1) "name"
2) "zaccoding"
3) "hobby"
4) "code"
5) "major"
6) "math"
```  

> HSETNX key field value  
=> hash의 field가 존재하지 않을 때에만 setting  

```
127.0.0.1:6379> HMSET zac name "zaccoding"
OK
127.0.0.1:6379> HSETNX zac name "zacscoding"
(integer) 0
127.0.0.1:6379> HSETNX zac hobby "coding"
(integer) 1
127.0.0.1:6379> HGETALL zac
1) "name"
2) "zaccoding"
3) "hobby"
4) "coding"
```  

> HVALS key  
=> hash에 존재하는 모든 값을 가져옴  

```
127.0.0.1:6379> HMSET zac name "zaccoding" hobby "coding" age 19
OK
127.0.0.1:6379> HVALS zac
1) "zaccoding"
2) "coding"
3) "19"
```  

[Spring-Data-Redis test  code](https://github.com/zacscoding/spring-boot-example/blob/master/springboot-redis-demo/src/test/java/org/zaccoding/document/HashesTest.java)






























<br /><br /><br /><br /><br /><br /><br /><br /><br />
<br /><br /><br /><br /><br /><br /><br /><br /><br />
