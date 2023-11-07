/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itec.controller;

import com.google.protobuf.TextFormat;
import dao.Conexion;
import dao.JornadaLaboralOperadorJpaController;
import dao.MunicipalidadJpaController;
import dao.TipoJornadaLaboralJpaController;
import dao.UsuarioJpaController;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.JornadaLaboralOperador;
import model.Municipalidad;
import model.Usuario;

/**
 * Controlador de Login de Usuario
 *
 */
public class LoginController {

    //DAO
    private final UsuarioJpaController usuarioDAO;
    //Model
    private static Usuario usuarioRegistradoInstanciaUnica = null;

    public LoginController() {
        //Inicializacion de DAO
        this.usuarioDAO = new UsuarioJpaController(Conexion.getEmf());
    }

    /**
     * Creacion Singleton Usuario Logeado
     */
    private synchronized static void createInstanceUsuario() {
        if (usuarioRegistradoInstanciaUnica == null) {
            usuarioRegistradoInstanciaUnica = new Usuario();
        }
    }

    /**
     * Devuele la instancia unica del usuario logeado
     *
     * @return
     */
    public static Usuario getInstanceUsuario() {
        createInstanceUsuario();
        return usuarioRegistradoInstanciaUnica;
    }

    /**
     * Creacion Singleton organismo
     */
    private synchronized static void createInstanceOrganismo() {
        if (organismoInstanciaUnica == null) {
            organismoInstanciaUnica = new Municipalidad();
        }
    }

    /**
     * Devuele la instancia unica del Municipalidad
     *
     * @return
     */
    public static Municipalidad getInstanceMunicipalidad() {
        createInstanceUsuario();
        return organismoInstanciaUnica;
    }

    /**
     * Recibe un usuario desde la vista Si el usuario y el password concuerdan,
     * crea instancia unica de usuario proveniente de la base de datos y
     * devuelve verdadero Si el usuario no concuerda devuelve falso
     *
     * @param unUsuario
     * @return
     */
    public boolean iniciarSesion(Usuario unUsuario) {
        boolean estado = false;
        usuarioRegistradoInstanciaUnica = usuarioDAO.iniciarSesion(unUsuario);
        if (usuarioRegistradoInstanciaUnica != null) {
            try {
                //Instacia Unica Singleton
                createInstanceUsuario();
                estado = true;

                JornadaLaboralOperador jlop = jornadaLaboralOperadorDAO.buscarInicioJornadaLaboralOperador(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA(), usuarioRegistradoInstanciaUnica.getUnOperador(), this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(1l), Conexion.getFechaActual());

                //Se verifica que es el primer inicio de sesion para ingresarlo al registro de jornada laboral
                if (jlop == null) {
                    System.out.println("Nueva Jornada Laboral");
                    this.unaJornadaLabora = new JornadaLaboralOperador();

                    this.unaJornadaLabora.setUnDepartamentoD(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA());
                    this.unaJornadaLabora.setUnOperador(usuarioRegistradoInstanciaUnica.getUnOperador());
                    //Tipo de jornada laboral Login
                    this.unaJornadaLabora.setUnTipoJornadaLaboral(this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(1l));
                    this.unaJornadaLabora.setUnaFecha(Conexion.getFechaActual());
                    this.unaJornadaLabora.setUnaHora(Conexion.getFechaActual());

                    this.jornadaLaboralOperadorDAO.create(unaJornadaLabora);
                } else {
                    System.out.println("Ya inicio Jornada Laboral");

                    jlop = jornadaLaboralOperadorDAO.buscarInicioJornadaLaboralOperador(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA(), usuarioRegistradoInstanciaUnica.getUnOperador(), this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(4l), Conexion.getFechaActual());
                    if (jlop != null) {
                        //Volvio de almorzar
                        jlop = jornadaLaboralOperadorDAO.buscarInicioJornadaLaboralOperador(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA(), usuarioRegistradoInstanciaUnica.getUnOperador(), this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(3l), Conexion.getFechaActual());
                        if (jlop != null) {

                            System.out.println("Continua Jornada Laboral volvio de almorzar");
                            this.unaJornadaLabora = new JornadaLaboralOperador();

                            this.unaJornadaLabora.setUnDepartamentoD(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA());
                            this.unaJornadaLabora.setUnOperador(usuarioRegistradoInstanciaUnica.getUnOperador());
                            //Tipo de jornada laboral Login
                            this.unaJornadaLabora.setUnTipoJornadaLaboral(this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(4l));
                            this.unaJornadaLabora.setUnaFecha(Conexion.getFechaActual());
                            this.unaJornadaLabora.setUnaHora(Conexion.getFechaActual());

                            this.jornadaLaboralOperadorDAO.create(unaJornadaLabora);
                        } else {
                            System.out.println("Ya comenzo a trabajar y ya almorzo");
                        }
                    }else{
                        System.out.println("ya se deslogeo no puede cargar mas registros en el dia");
                    }

                }
            } catch (TextFormat.ParseException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return estado;
    }

    public boolean almorzar() {
        boolean estado = false;
        if (usuarioRegistradoInstanciaUnica != null) {
            try {

                JornadaLaboralOperador jlop = jornadaLaboralOperadorDAO.buscarInicioJornadaLaboralOperador(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA(), usuarioRegistradoInstanciaUnica.getUnOperador(), this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(4l), Conexion.getFechaActual());

                if (jlop == null) {
                    jlop = jornadaLaboralOperadorDAO.buscarInicioJornadaLaboralOperador(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA(), usuarioRegistradoInstanciaUnica.getUnOperador(), this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(3l), Conexion.getFechaActual());

                    //Se verifica que es el primer inicio de sesion para ingresarlo al registro de jornada laboral
                    if (jlop == null) {

                        System.out.println("Nuevo Almuezo");
                        this.unaJornadaLabora = new JornadaLaboralOperador();

                        this.unaJornadaLabora.setUnDepartamentoD(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA());
                        this.unaJornadaLabora.setUnOperador(usuarioRegistradoInstanciaUnica.getUnOperador());
                        //Tipo de jornada laboral Login
                        this.unaJornadaLabora.setUnTipoJornadaLaboral(this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(3l));
                        this.unaJornadaLabora.setUnaFecha(Conexion.getFechaActual());
                        this.unaJornadaLabora.setUnaHora(Conexion.getFechaActual());

                        this.jornadaLaboralOperadorDAO.create(unaJornadaLabora);
                        estado = true;
                    } else {
                        System.out.println("Ya almorzo");

                    }
                } else {
                    System.out.println("ya se deslogeo no puede cargar mas registros en el dia");
                }

            } catch (TextFormat.ParseException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return estado;
    }

    public boolean finalizarJornadaLaboral() {
        boolean estado = false;
        if (usuarioRegistradoInstanciaUnica != null) {
            try {

                JornadaLaboralOperador jlop = jornadaLaboralOperadorDAO.buscarInicioJornadaLaboralOperador(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA(), usuarioRegistradoInstanciaUnica.getUnOperador(), this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(2l), Conexion.getFechaActual());

                //Se verifica que es el primer inicio de sesion para ingresarlo al registro de jornada laboral
                if (jlop == null) {

                    System.out.println("Nuevo Finalizacion de Jornada Laboral");
                    this.unaJornadaLabora = new JornadaLaboralOperador();

                    this.unaJornadaLabora.setUnDepartamentoD(usuarioRegistradoInstanciaUnica.getUnOperador().getUnDepartamentoA());
                    this.unaJornadaLabora.setUnOperador(usuarioRegistradoInstanciaUnica.getUnOperador());
                    //Tipo de jornada laboral Login
                    this.unaJornadaLabora.setUnTipoJornadaLaboral(this.tipoJornadaLaboralDAO.findTipoJornadaLaboral(2l));
                    this.unaJornadaLabora.setUnaFecha(Conexion.getFechaActual());
                    this.unaJornadaLabora.setUnaHora(Conexion.getFechaActual());

                    this.jornadaLaboralOperadorDAO.create(unaJornadaLabora);
                    estado = true;
                } else {
                    System.out.println("Ya Finalizo la jornada Laboral");

                }
            } catch (TextFormat.ParseException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return estado;
    }

}
