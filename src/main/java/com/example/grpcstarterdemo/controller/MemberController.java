package com.example.grpcstarterdemo.controller;


import com.example.grpcdemo.grpc.*;
import com.example.grpcstarterdemo.vo.GreeterVo;
import com.example.grpcstarterdemo.vo.MemberVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController{

    private MemberListServiceGrpc.MemberListServiceBlockingStub memberListServiceBlockingStub;
    private MemberLoginServiceGrpc.MemberLoginServiceBlockingStub memberLoginServiceBlockingStub;
    private GreeterGrpc.GreeterBlockingStub greeterBlockingStub;

    public MemberController(MemberListServiceGrpc.MemberListServiceBlockingStub memberListServiceBlockingStub,
                            MemberLoginServiceGrpc.MemberLoginServiceBlockingStub memberLoginServiceBlockingStub,
                            GreeterGrpc.GreeterBlockingStub greeterBlockingStub) {
        this.memberListServiceBlockingStub = memberListServiceBlockingStub;
        this.memberLoginServiceBlockingStub = memberLoginServiceBlockingStub;
        this.greeterBlockingStub = greeterBlockingStub;
    }

    /**
     * curl -X POST -H "Content-type: application/json" -d "{\"username\":\"freewolf\", \"password\":\"123456\"}" http://localhost:8888/login
     *
     * @param memberVO
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestBody MemberVO memberVO){
        MemberLoginResponse response = this.memberLoginServiceBlockingStub.memberLogin(MemberLoginRequest.newBuilder()
                .setMember(Member.newBuilder()
                        .setPassword(memberVO.getPassword())
                        .setUsername(memberVO.getUsername())
                        .build())
                .build());
        return response.getToken();
    }

    @GetMapping("/hello")
    public String hello(GreeterVo greeterVo){
        GreeterRequest buildRequest = GreeterRequest.newBuilder().setName(greeterVo.getName()).build();

        GreeterResponse hello = this.greeterBlockingStub.hello(buildRequest);
        return hello.getGreeting();
    }

    @PostMapping("/hello2")
    public String hello2(@RequestBody GreeterVo greeterVo){
        GreeterRequest buildRequest = GreeterRequest.newBuilder().setName(greeterVo.getName()).build();

        GreeterResponse hello = this.greeterBlockingStub.hello(buildRequest);
        return hello.getGreeting();
    }

    /**
     * curl -g -i -X GET http://localhost:8888/list
     * @return
     */
    @GetMapping("/list")
    public List<MemberVO> list(){
        MemberListResponse response = this.memberListServiceBlockingStub.memberList(MemberListRequest.newBuilder()
                .setPage(1)
                .setPerPage(2)
                .build());
        List<MemberVO> list = MemberVO.getList(response.getMemberList());
        return list;
    }
}
