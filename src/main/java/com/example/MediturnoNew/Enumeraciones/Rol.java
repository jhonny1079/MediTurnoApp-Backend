package com.example.MediturnoNew.Enumeraciones;

public enum Rol {

    ROLE_ADMINISTRADOR,   //Acceso total, gestion de usuarios y reportes
    ROLE_PACIENTE,       //Puede gestionar sus citas y datos personales
    ROLE_MEDICO ,         //puede ver su agenda y pacientes asignados
    ROLE_RECEPCIONITA     // puede agendar/cancelar citas para cualquier paciente
}
