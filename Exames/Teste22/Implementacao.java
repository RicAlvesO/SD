package Exames.Teste22;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Implementacao implements Votacao{
    private Eleitores eleitores;
    private Cabines cabines;
    private Candidatos candidatos;
    private ReentrantLock lock=new ReentrantLock();

    private{ 
        private int nEleitores;
        private ReentrantLock lock = new ReentrantLock();
        private Condition cond = new this.lock.newCondition();
    }

    private class Eleitores{
        private List<Integer,Boolean> eleitores = new ArrayList<Integer,Boolean>();
        private ReentrantLock lock = new ReentrantLock();
        private Condition cond = new this.lock.newCondition();
    }
    
    private class Candidatos{
        private Map<Integer,Candidato> candidatos = new HashMap<Integer,Candidato>();
        private ReentrantLock lock = new ReentrantLock();
        private Condition cond = new this.lock.newCondition();
    }

    private class Candidato{
        private int id;
        private int votos;
        private ReentrantLock lock = new ReentrantLock();
        private Condition cond = new this.lock.newCondition();
    }

    private class Cabines{
        private List<Integer> cabines = new ArrayList<Integer>();
        private ReentrantLock lock = new ReentrantLock();
        private Condition cond = new this.lock.newCondition();
        private Stack<Integer> free_stack = new Stack<Integer>();
    }

    public boolean verifica(int identidade){
        this.eleitores.lock.lock();
        if(this.eleitores.eleitores.containsKey(identidade)){
            if (this.eleitores.eleitores.get(identidade) == false){
                this.eleitores.eleitores.put(identidade, true);
                this.votos.lock.lock();
                this.eleitores.lock.unlock();
                this.votos.nEleitores++;
                this.votos.lock.unlock();
                return true;
            }
        }
        return false;
    }
    public int esperaPorCabine(){
        this.cabines.lock.lock();
        while(this.cabines.free_stack.empty()){
            this.cabines.cond.await();
        }
        int cabine = this.cabines.free_stack.pop();
        this.cabines.lock.unlock();
        return cabine;
    }

    public void vota(int escolha){
        if this.candidatos.candidatos.containsKey(escolha){
            this.candidatos.candidatos.get(escolha).lock.lock();
            this.candidatos.candidatos.get(escolha).votos++;
            this.votos.lock.lock();
            this.candidatos.candidatos.get(escolha).lock.unlock();
            this.votos.nEleitores--;
            this.votos.lock.unlock();
        }
    }

    public void desocupaCabine(int i){
        this.cabines.lock.lock();
        this.cabines.free_stack.push(i);
        this.cabines.cond.signal();
        this.cabines.lock.unlock();
    }
    
    public int vencedor(){
        this.eleitores.lock.lock();
        this.votos.lock.lock();
        while(this.votos.nEleitores > 0){
            this.votos.cond.await();
        }
        this.votos.lock.unlock();
        int max = 0;
        this.candidatos.lock.lock();
        //fazer get max do mapa de eleitores, not that important
        this.candidatos.lock.unlock();
        this.eleitores.lock.unlock();
        return max;
    }

}
