package com.shedhack.tool.spring.boost;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <pre>
 *     Enables Spring Boost libraries.
 * </pre>
 *
 * @author imamchishty
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Import(SpringBoostConfiguration.class)
public @interface EnableSpringBoost {
}
