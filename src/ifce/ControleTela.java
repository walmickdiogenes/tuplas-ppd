/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ifce;

import java.rmi.RemoteException;
import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

/**
 *
 * @author walmi
 */
public class ControleTela {

    private JavaSpace space;
    Icon excluirAmbiente;

    public ControleTela() {
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            Lookup finder = new Lookup(JavaSpace.class);
            this.space = (JavaSpace) finder.getService();
            if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("O servico JavaSpace foi encontrado.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public String criarAmbiente() {
        String finalName;
        try {
            Ambiente novoAmb = new Ambiente();
            int ambIndex = 0;

            while (true) {
                ambIndex += 1;
                String ambName = "amb" + ambIndex;
                novoAmb.nome = ambName;
                Ambiente tempAmb = (Ambiente) this.space.read(novoAmb, null, JavaSpace.NO_WAIT);

                if (tempAmb == null) {
                    this.space.write(novoAmb, null, Lease.FOREVER);
                    System.out.println("\r\nAmbiente " + ambName + " criado");
                    finalName = ambName;
                    break;
                }

            }
            return finalName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String criarDispositivo() {
        String finalName;
        try {
            Dispositivo novoDisp = new Dispositivo();
            int dispIndex = 0;

            while (true) {
                dispIndex += 1;
                String dispName = "disp" + dispIndex;
                novoDisp.nome = dispName;
                Dispositivo tempDisp = (Dispositivo) space.read(novoDisp, null, JavaSpace.NO_WAIT);

                if (tempDisp == null) {
                    space.write(novoDisp, null, Lease.FOREVER);
                    System.out.println("\r\nDispositivo " + dispName + " criado");
                    finalName = dispName;
                    break;
                }
            }
            return finalName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void moverDispositivo(String nome, String novoAmb) {
        try {
            Dispositivo oldDisp = new Dispositivo();
            oldDisp.nome = nome;
            Dispositivo disp = (Dispositivo) space.take(oldDisp, null, JavaSpace.NO_WAIT);

            disp.amb = novoAmb;
            space.write(disp, null, Lease.FOREVER);
            System.out.println("\r\nDispositivo " + nome + " movido para o ambiente " + novoAmb);

        } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
            Logger.getLogger(ControleTela.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String listarDispositivo(String ambNome) {
        String nomesDispositivos = "";
        String nomesDispositivosNull = "Não existe nenhum dispositivo no ambiente";
        try {
            List<Dispositivo> listaDisp = Helpers.listaDispositivo(space, ambNome);
            for (int i = 0; i < listaDisp.size(); i++) {
                nomesDispositivos += listaDisp.get(i).nome;
                if (i < listaDisp.size() - 1) {
                    nomesDispositivos += ", ";
                }
            }
            System.out.println(nomesDispositivos);
            nomesDispositivos = "Esses são os dispositivos que estão no ambiente: " + nomesDispositivos;

            if (listaDisp.size() == 0) {
                nomesDispositivos = nomesDispositivosNull;
            }

            listaDisp.clear();
        } catch (Exception ex) {
            Logger.getLogger(ControleTela.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nomesDispositivos;
    }
    
    public void excluirAmbiente () {
        System.out.println("excluit");
    }
    
    public void excluirDispositivo () {
        System.out.println("excluit");
    }
}

