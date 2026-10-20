package com.salesianos.registro_jornadas_api.error.notfound;

import jakarta.persistence.EntityNotFoundException;

public class DepartmentNotFoundException extends EntityNotFoundException {

    //Se le pasa un message por el constructo
    public DepartmentNotFoundException(String message) {
        super(message);
    }

  //El handler nunca sabe cual de los tres msj tiene que devolver
  //Lo elige el codigo que lanza la excepción, en el momento de hacer el new.
  //Dependiendo lo que le pases como argumento, eligirá un constructor u otro
  public DepartmentNotFoundException() {
    super("departments not found ");

  }

  public DepartmentNotFoundException(Long id){


  super("Department not found with id " + id);

  }





}
