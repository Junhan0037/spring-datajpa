package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names); // 컬렉션 파라미터 바인딩

    List<Member> findListByUsername(String name); // 컬렉션
    Member findMemberByUsername(String name); // 단건
    Optional<Member> findOptionalByUsername(String name); // 단건 Optional

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m") // 쿼리 따로 작성 적용 가능
    Page<Member> findByAge(int age, Pageable pageable); // JPA 페이징과 정렬

    @Modifying(clearAutomatically = true) // em.clear() 기능
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age); // 벌크성 수정 쿼리

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin(); // 페치 조인

    @Override
    @EntityGraph(attributePaths = {"team"}) // 페치 조인을 지원
    List<Member> findAll(); // @EntityGraph

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph(); // JPQL에 EntityGraph 추가하기

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

}
