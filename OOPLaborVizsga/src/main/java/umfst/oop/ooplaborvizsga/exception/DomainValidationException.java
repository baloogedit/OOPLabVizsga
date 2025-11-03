/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga.exception;

/**
 *
 * @author edite
 */
public class DomainValidationException extends Exception{
    
    public DomainValidationException(String message) {
        super(message);
    }
    
    public DomainValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
