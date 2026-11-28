package com.example.board.support;

import java.util.Locale;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 프론트(템플릿)는 아직 없으므로, 컨트롤러 테스트에서 Thymeleaf가 실제 템플릿을
 * 찾다 실패하지 않도록 뷰 이름만 돌려주는 가짜 ViewResolver를 최우선 순위로 등록한다.
 * "redirect:" 뷰는 실제 RedirectView로 처리해 리다이렉트 검증은 그대로 동작시킨다.
 */
@TestConfiguration
public class StubViewResolverConfig {

    @Bean
    public ViewResolver stubViewResolver() {
        return new OrderedStubViewResolver();
    }

    private static class OrderedStubViewResolver implements ViewResolver, Ordered {

        @Override
        public View resolveViewName(String viewName, Locale locale) {
            if (viewName.startsWith("redirect:")) {
                return new RedirectView(viewName.substring("redirect:".length()));
            }
            return (model, request, response) -> response.setStatus(200);
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }
    }
}
