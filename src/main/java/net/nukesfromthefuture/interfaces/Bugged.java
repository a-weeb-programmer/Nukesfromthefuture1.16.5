package net.nukesfromthefuture.interfaces;

public @interface Bugged {
    public String bug();
    public String possible_solutions() default "none yet";
}
