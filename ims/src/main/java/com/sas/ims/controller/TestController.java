package com.sas.ims.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;

@RestController
@RequestMapping("/auth")
public class TestController {

    @GetMapping("/cookiee")
    public void setCookiee(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        setCookie(httpServletResponse);
    }

    private void setCookie(HttpServletResponse response) {
        //Tue, 04-Apr-2023 10:15:01 GMT
        SimpleDateFormat cookieExpireFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz");
        cookieExpireFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10000);
        String cookieLifeTime = cookieExpireFormat.format(cal.getTime());
        response.setHeader(HttpHeaders.SET_COOKIE,
                "token=".concat("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW5kZWVwLnJhd2F0QHNhc3RlY2hzdHVkaW8uY29tIiwiaWF0IjoxNjgxMTk2Mjg1LCJleHAiOjE2ODExOTc3MjUsImF1dGhvcml0aWVzIjp7IkxPUyI6WyJWRU5ET1JfRURJVCIsIlZFTkRPUl9BREQiLCJWRU5ET1JfTElTVCIsIlBST0RVQ1RfRURJVCIsIlBST0RVQ1RfQUREIiwiUFJPRFVDVF9MSVNUIiwiUFJPRFVDVF9HUk9VUF9FRElUIiwiUFJPRFVDVF9HUk9VUF9BREQiLCJQUk9EVUNUX0dST1VQX0xJU1QiLCJBUkVBX0VESVQiLCJBUkVBX0FERCIsIkFSRUFfTElTVCIsIlVTRVJfRURJVCIsIlVTRVJfTElTVCIsIlVTRVJfQUREIiwiQURNSU4iXSwiTE1TIjpbIlZFTkRPUl9FRElUIiwiVkVORE9SX0FERCIsIlZFTkRPUl9MSVNUIiwiUFJPRFVDVF9FRElUIiwiUFJPRFVDVF9BREQiLCJQUk9EVUNUX0xJU1QiLCJQUk9EVUNUX0dST1VQX0VESVQiLCJQUk9EVUNUX0dST1VQX0FERCIsIlBST0RVQ1RfR1JPVVBfTElTVCIsIkFSRUFfRURJVCIsIkFSRUFfQUREIiwiQVJFQV9MSVNUIiwiVVNFUl9FRElUIiwiVVNFUl9MSVNUIiwiVVNFUl9BREQiLCJBRE1JTiJdLCJJTVMiOlsiVkVORE9SX0VESVQiLCJWRU5ET1JfQUREIiwiVkVORE9SX0xJU1QiLCJQUk9EVUNUX0VESVQiLCJQUk9EVUNUX0FERCIsIlBST0RVQ1RfTElTVCIsIlBST0RVQ1RfR1JPVVBfRURJVCIsIlBST0RVQ1RfR1JPVVBfQUREIiwiUFJPRFVDVF9HUk9VUF9MSVNUIiwiQVJFQV9FRElUIiwiQVJFQV9BREQiLCJBUkVBX0xJU1QiLCJVU0VSX0VESVQiLCJVU0VSX0xJU1QiLCJVU0VSX0FERCIsIkFETUlOIl19LCJhcHBzIjpbIkxPUyIsIkxNUyIsIklNUyJdLCJhcHBJbmZvIjp7IkxPUyI6eyJjcmVhdGVkQnkiOjEsImNyZWF0ZWREYXRlIjoxNjgwMTM0NDAwMDAwLCJ1cGRhdGVkQnkiOjEsInVwZGF0ZWRPbiI6MTY4MDEzNDQwMDAwMCwiaWQiOjMsImNvbXBhbnlJZCI6MSwiYXBwbGljYXRpb25OYW1lIjoiTE9TIiwiYmFzZVVybCI6Imh0dHA6Ly9sb2NhbGhvc3Q6MzAwMCIsImFjdGl2ZSI6dHJ1ZSwibG9nb1VybCI6bnVsbH0sIkxNUyI6eyJjcmVhdGVkQnkiOjEsImNyZWF0ZWREYXRlIjoxNjgwMTM0NDAwMDAwLCJ1cGRhdGVkQnkiOjEsInVwZGF0ZWRPbiI6MTY4MDEzNDQwMDAwMCwiaWQiOjIsImNvbXBhbnlJZCI6MSwiYXBwbGljYXRpb25OYW1lIjoiTE1TIiwiYmFzZVVybCI6Imh0dHA6Ly9XV1cuR09PR0xFLkNPTSIsImFjdGl2ZSI6dHJ1ZSwibG9nb1VybCI6bnVsbH0sIklNUyI6eyJjcmVhdGVkQnkiOjEsImNyZWF0ZWREYXRlIjoxNjgwMTM0NDAwMDAwLCJ1cGRhdGVkQnkiOjEsInVwZGF0ZWRPbiI6MTY4MDEzNDQwMDAwMCwiaWQiOjEsImNvbXBhbnlJZCI6MSwiYXBwbGljYXRpb25OYW1lIjoiSU1TIiwiYmFzZVVybCI6Imh0dHA6Ly9sb2NhbGhvc3Q6MzAwMCIsImFjdGl2ZSI6dHJ1ZSwibG9nb1VybCI6bnVsbH19LCJjb21wYW55Ijp7ImNyZWF0ZWRCeSI6MSwiY3JlYXRlZERhdGUiOjE2ODAxMzQ0MDAwMDAsInVwZGF0ZWRCeSI6MSwidXBkYXRlZE9uIjoxNjgwMTM0NDAwMDAwLCJjb21wYW55SWQiOjEsImNvbXBhbnlOYW1lIjoiQUlRQSIsImNvbXBhbnlDb2RlIjoiY29tcGFueV8xIiwid2Vic2l0ZVVybCI6Ind3dy5haXFhaGVhbHRoLmNvbSIsImFjdGl2ZSI6dHJ1ZX0sInRlbmFudElkIjoiVlJSTWFpU2s2ZzZxcnNqSFJ3dGs1dz09In0.nXUcd8VZ5j-xsJCb90flayYA0hiThM822co5evX5V28")
                        .concat("; Path=/; Expires=" + cookieLifeTime + ";"));
    }

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from Spring Boot Application");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public ResponseEntity<String> testMapping() {
        return ResponseEntity.ok("Authenticated for role ADMIN");
    }

}
