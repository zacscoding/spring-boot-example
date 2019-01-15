# Testing elastic/apm-agent-java  

> ## Getting started  

```aidl
$ ./mvnw clean install
$ java -javaagent:lib/elastic-apm-agent-1.3.0.jar -Delastic.apm.service_name=my-cool-service -Delastic.apm.application_packages=demo.person -Delastic.apm.server_urls=http://127.0.0.1:8080 -jar target/demo.jar
```  

=> Connect here http://localhost:8080/swagger-ui.html#  

---  

> ## Analyze report data  

- **metadata**  

```aidl
{
  "metadata": {
    "service": {
      "name": "my-cool-service",
      "agent": {
        "name": "java",
        "version": "1.3.0"
      },
      "language": {
        "name": "Java",
        "version": "1.8.0_161"
      },
      "runtime": {
        "name": "Java",
        "version": "1.8.0_161"
      },
      "version": null
    },
    "process": {
      "pid": 13916,
      "title": "C:\\Program Files\\Java\\jdk1.8.0_161\\jre\\bin\\java.exe"
    },
    "system": {
      "architecture": "amd64",
      "hostname": "DESKTOP-497E2KO",
      "platform": "Windows 10"
    }
  }
}
```  

- **transaction**  

```aidl
{
  "transaction": {
    "timestamp": 1547519848925002,
    "name": "PersonController#getPersons",
    "id": "5f42c437e81495f8",
    "trace_id": "518d6d565c64bb962e0f6a307e3ac31e",
    "type": "request",
    "duration": 282.111,
    "result": "HTTP 2xx",
    "context": {
      "request": {
        "method": "GET",
        "headers": {
          "host": "localhost:8080",
          "connection": "keep-alive",
          "cache-control": "max-age=0",
          "upgrade-insecure-requests": "1",
          "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
          "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
          "accept-encoding": "gzip, deflate, br",
          "accept-language": "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7"
        },
        "cookies": {
          "Idea-1c2d522": "023d3f1d-b489-43fd-b347-1e5fdc9350e4",
          "Idea-d1852ee0": "1ab9f695-c195-4582-837e-f05b32facaa6"
        },
        "url": {
          "full": "http://localhost:8080/person",
          "hostname": "localhost",
          "port": "8080",
          "pathname": "/person",
          "protocol": "http"
        },
        "socket": {
          "encrypted": false,
          "remote_address": "0:0:0:0:0:0:0:1"
        },
        "http_version": "1.1"
      },
      "response": {
        "headers": {
          "Content-Type": "application/json;charset=UTF-8",
          "Transfer-Encoding": "chunked",
          "Date": "Tue, 15 Jan 2019 02:37:28 GMT"
        },
        "finished": true,
        "headers_sent": true,
        "status_code": 200
      },
      "tags": {
        
      }
    },
    "span_count": {
      "dropped": 0,
      "started": 11
    },
    "sampled": true
  }
}
// span pretty sample
{
  "span": {
    "name": "SELECT",
    "timestamp": 1547519849100049,
    "id": "01e356926a2b3524",
    "trace_id": "518d6d565c64bb962e0f6a307e3ac31e",
    "transaction_id": "5f42c437e81495f8",
    "parent_id": "5f42c437e81495f8",
    "duration": 4.422,
    "context": {
      "db": {
        "statement": "select person0_.id as id1_0_, person0_.name as name2_0_ from person person0_ limit ?",
        "type": "sql",
        "user": "SA"
      }
    },
    "type": "db.h2.query"
  }
}

// spans
{"span":{"name":"SELECT","timestamp":1547519849100049,"id":"01e356926a2b3524","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":4.422,"context":{"db":{"statement":"select person0_.id as id1_0_, person0_.name as name2_0_ from person person0_ limit ?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849191697,"id":"67e38b4542c3acc5","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.304,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849200515,"id":"cb4c672debadf99b","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.202,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849200996,"id":"baf14d656f8dce3d","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.137,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849201321,"id":"9a35eaabfe251bc5","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.091,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849201725,"id":"b986336984ee52dd","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.095,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849201968,"id":"2e54bd56381fb856","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.126,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849202412,"id":"d0e7ac959ffa725d","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.085,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849202694,"id":"f0e32676e3227416","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.116,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849203010,"id":"9848baed2e5b09db","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.098,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
{"span":{"name":"SELECT","timestamp":1547519849203383,"id":"f3cf787b5d2b13a5","trace_id":"518d6d565c64bb962e0f6a307e3ac31e","transaction_id":"5f42c437e81495f8","parent_id":"5f42c437e81495f8","duration":0.075,"context":{"db":{"statement":"select hobbies0_.person_id as person_i1_1_0_, hobbies0_.hobbies as hobbies2_1_0_ from person_hobbies hobbies0_ where hobbies0_.person_id=?","type":"sql","user":"SA"}},"type":"db.h2.query"}}
```  

- **metricset**  

```aidl
{
  "metricset": {
    "timestamp": 1547520367640000,
    "samples": {
      "system.process.cpu.total.norm.pct": {
        "value": 0.002214069552509608
      },
      "jvm.memory.heap.used": {
        "value": 2.6090716E8
      },
      "jvm.memory.non_heap.used": {
        "value": 8.1117784E7
      },
      "jvm.memory.heap.max": {
        "value": 7.6152832E9
      },
      "jvm.gc.alloc": {
        "value": 7.6968816E7
      },
      "jvm.memory.non_heap.committed": {
        "value": 8.4819968E7
      },
      "system.cpu.total.norm.pct": {
        "value": 0.07315489256880947
      },
      "system.process.memory.size": {
        "value": 1.565646848E9
      },
      "jvm.memory.heap.committed": {
        "value": 9.69408512E8
      },
      "jvm.memory.non_heap.max": {
        "value": -1.0
      },
      "system.memory.actual.free": {
        "value": 2.0677500928E10
      },
      "system.memory.total": {
        "value": 3.4266791936E10
      }
    }
  }
}

{
  "metricset": {
    "timestamp": 1547520367640000,
    "tags": {
      "name": "PS Scavenge"
    },
    "samples": {
      "jvm.gc.time": {
        "value": 90.0
      },
      "jvm.gc.count": {
        "value": 9.0
      }
    }
  }
}


{
  "metricset": {
    "timestamp": 1547520367640000,
    "tags": {
      "name": "PS MarkSweep"
    },
    "samples": {
      "jvm.gc.time": {
        "value": 292.0
      },
      "jvm.gc.count": {
        "value": 3.0
      }
    }
  }
}
```



