package net.epoxide.permissions.common.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation designating a field to be populated with the PermissionsRegistry.
 * 
 * @author Ghostrec35
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PermissionHandler 
{
}
