package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) { // 도메인 클래스 컨버터
        return member.getUsername();
    }

    @GetMapping("/members") // http://localhost:8080/members?page=?&size=?&sort=id.desc
    public Page<MemberDto> list(Pageable pageable) { // 엔티티 직접 노출은 위험하다
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }

//    @PostConstruct
//    public void init() {
//        memberRepository.save(new Member("userA"));
//    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }

}
