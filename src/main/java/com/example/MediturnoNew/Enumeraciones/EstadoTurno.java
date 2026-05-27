package com.example.MediturnoNew.Enumeraciones;

public enum EstadoTurno {

    PENDIENTE,  //Recien creado, esperando confirmacion opcional
    CONFIRMADO,  // turno confirmado por el sistema o el paciente
    PRESENTE,    // el paciente se registro al llegar al centro de salud
    EN_CURSO,   // el medico esta atendiendo al paciente
    ATENDIDO,   //la consulta finalizo exitosamente
    CANCELADO,   //cancelado por el paciente o medico
    NO_ASISTIO   //el paciente no se presento a la cita
}
