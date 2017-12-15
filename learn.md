# Index

- <a href="#lombok">Lombok Attributes</a>

---



<div id="lombok"></div>

<a href="https://projectlombok.org/features/all">Lombok features all </a>

<table>
  <tr>
    <th>@NonNull</th>
    <td>Null값이 될 수 없음. NullPointerException에 대한 대비책이 될 수 있음</td>
  </tr>
  <tr>
    <th>@Cleanup</th>
    <td>자동으로 close() 메소드를 호출하는 역할을 함</td>
  </tr>
  <tr>
    <th>@Getter/@Setter</th>
    <td>코드가 컴파일될 때 속성들에 대해 Getter/Setter 메소드 들을 생성</td>
  </tr>
  <tr>
    <th>@ToString</th>
    <td>toString() 메소드 생성</td>
  <tr>
    <th>@EqualsAndHashCode</th>
    <td>해당 객체의 equals()와 hashCode() 메소드를 생성</td>
  </tr>  
  <tr>
    <th>@NoArgsConstructor</th>
    <td>파라미터 없는 생성자</td>
  </tr>
  <tr>
    <th>@RequiredConstructor</th>
    <td>지정된 속성들에 대한 생성자</td>
  </tr>
  <tr>
    <th>@AllArgsConstructor</th>
    <td>모든 속성에 대한 생성자</td>
  </tr>
  <tr>
    <th>@Data</th>
    <td>@ToString, @EqualsAndHashCode, @Getter(모든속성), @Setter(final이 아닌 속성) <br />
        @RequiredConstructor를 합친 어노테이션
    </td>
  </tr>
  <tr>
    <th>@Value</th>
    <td>불면(immutable) 클래스를 생성</td>
  </tr>
  <tr>
    <th>@Log</th>
    <td>자동으로 생기는 log라는 변수를 이용해서 로그를 찍을 수 있음</td>
  </tr>
  <tr>
    <th>@Builder</th>
    <td>빌더 패턴을 사용할 수 있도록 코드를 생성 <br>
        new AA().setA().setB().setC()와 같이 체이닝 할 수 있는 코드 생성
    </td>
  </tr>
  <tr>
    <th>@SneakyThrows</th>
    <td>예외 발생 시 Throwable 타입으로 반환</td>
  </tr>
  <tr>
    <th>@Synchronized</th>
    <td>메소드에서 동기화를 설정</td>
  </tr>
  <tr>
    <th>@Getter(laze=true)</th>
    <td>동기화를 이용해서 최초 한 번만 getter를 호출</td>
  </tr>  
</table>

---
