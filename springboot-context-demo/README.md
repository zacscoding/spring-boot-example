## Spring Context test

- <a href="#lazy-bean">@Lazy</a>  
- <a href="#ApplicationContext">ApplicationContext</a>
- <a href="#Prototype">Prototype</a>


---  

<div id="lazy-bean"></div>

- Profile : lazy-load  
- Test url  
: http://localhost:8080/lazybean/bean1  
: http://localhost:8080/lazybean/bean2  
: http://localhost:8080/lazybean/bean3    
: http://localhost:8080/lazybean2  

---

<div id="ApplicationContext"></div>  

## After context loaded -> initialize  

- Profile : appctx  
- Test url : no
 

---  

<div id="Prototype"></div>  

## Tests prototype

- Profile : prototype  
- Test url : 
[GET]    : http://localhost:8080/prototypes
[GET]    : http://localhost:8080/prototypes/ctx
[GET]    : http://localhost:8080/prototype/{id}
[POST]   : http://localhost:8080/prototype
[DELETE] : http://localhost:8080/prototype/{id}

---
