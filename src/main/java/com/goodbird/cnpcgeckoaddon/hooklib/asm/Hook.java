package com.goodbird.cnpcgeckoaddon.hooklib.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Чтобы сделать метод хуком, нужно повесить над ним эту аннотацию и зарегистрировать класс с хуком.
 * <p/>
 * Целевой класс определяется первым параметром хук-метода. Если целевой метод static, то туда прилетает null,
 * иначе - this.
 * <p/>
 * Название целевого метода по умолчанию такое же, как название хук-метода, но его можно переопределить через
 * targetMethod.
 * <p/>
 * Список параметров целевого метода определяется списком параметров хук-метода. Нужно добавить все те же параметры
 * в том же порядке.
 * <p/>
 * Возвращаемый тип целевого метода по умолчанию не указывается вообще. Предполагается, что методов с одинаковым
 * названием и списком параметров нет. Если всё же нужно указать, то это можно сделать через returnType.
 */
@Target(ElementType.METHOD)
public @interface Hook {

    ReturnCondition returnCondition() default ReturnCondition.NEVER;

    HookPriority priority() default HookPriority.NORMAL;

    String targetMethod() default "";

    String returnType() default "";

    boolean createMethod() default false;


    boolean isMandatory() default false;

    boolean injectOnExit() default false;

    @Deprecated int injectOnLine() default -1;

    String returnAnotherMethod() default "";

    boolean returnNull() default false;


    boolean booleanReturnConstant() default false;

    byte byteReturnConstant() default 0;

    short shortReturnConstant() default 0;

    int intReturnConstant() default 0;

    long longReturnConstant() default 0L;

    float floatReturnConstant() default 0.0F;

    double doubleReturnConstant() default 0.0D;

    char charReturnConstant() default 0;

    String stringReturnConstant() default "";

    @Target(ElementType.PARAMETER)
    @interface LocalVariable {
        int value();
    }
    @Target(ElementType.PARAMETER)
    @interface ReturnValue {}
}
