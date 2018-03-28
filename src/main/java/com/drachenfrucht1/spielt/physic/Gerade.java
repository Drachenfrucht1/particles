package com.drachenfrucht1.spielt.physic;

import com.drachenfrucht1.spielt.Settings;

import java.util.ArrayList;
import java.util.List;

public class Gerade {
    float m;
    float c;
    float Defanf;
    float Defend;//Definitionsmenge Ende
    PhysicsObject Körper;
    float tanf=0;//Zeit, die bei evtl schon vorheriger Fusion bis zum Treffen vergangen ist

 Gerade(float m, float c, float Defanf, float Defend, PhysicsObject Körper){
     this.m=m;
     this.c=c;
     this.Defanf=Defanf;
     this.Defend=Defend;
     this.Körper=Körper;
 }

 public void gleichrichten(){ //sollte nach jeder Erstellung oder Veränderung einer Geraden durcgenommen werden, damit Defend und Defanf
     //nicht falsch herum stehen
     if(Defanf>Defend){
         float übertrag=Defanf;
         Defanf=Defend;
         Defend=übertrag;
     }
 }
 public void fusion(List<PhysicsObject>k){
     List<Gerade>ger=new ArrayList<Gerade>();
     for(int i=0; i<k.size();i++){
         float m1=k.get(i).getSpeed().y/k.get(i).getSpeed().getX();
         float c1=k.get(i).getY()-m1*k.get(i).getX();
         float Defanf1=k.get(i).getX();
         float Defend1=k.get(i).getX()+k.get(i).getSpeed().getX();
         Gerade hinzu=new Gerade(m1,c1,Defanf1,Defend1,k.get(i));
         ger.add(hinzu);
         //liste mit allen Körpern als Gerade
     }
     int i=0;
     while( i==0){
         Punkt test = new Punkt(1, 1, 0);//durchlaufender pkt
         Punkt aktuelltreffend = new Punkt(1, 1, 100000);//pkt mit kleinster Zeit
         if (test.getT() != 0 && test.getT() < aktuelltreffend.getT()) {
             aktuelltreffend = test;
         }
         if (test.getT() == 0) {
             //kein einziger Treffer
             i = 1;
         } else {
             //bearbeite ersten Treffer
             //EnergieVektoren
             Vector2D E1 = new Vector2D(0, 0);

             float E1länge = aktuelltreffend.getG11().Körper.getMass() * (float) Math.pow((double) aktuelltreffend.getG11().Körper.getVE(), 2) * 1 / 2;
             ;
             float x1 = E1länge / (float) Math.sqrt(Math.pow(aktuelltreffend.getG11().m, 2) + 1);
             float y1 = x1 * aktuelltreffend.getG11().m;

             float E2länge = aktuelltreffend.getG22().Körper.getMass() * (float) Math.pow((double) aktuelltreffend.getG22().Körper.getVE(), 2) * 1 / 2;
             ;
             float x2 = E2länge / (float) Math.sqrt(Math.pow(aktuelltreffend.getG22().m, 2) + 1);
             float y2 = x2 * aktuelltreffend.getG22().m;

             Vector2D Eresult = new Vector2D(x1 + x2, y1 + y2);
             float Einnere = E1länge + E2länge - Eresult.getValue();

             float vNeu = (float) Math.sqrt((double) Eresult.getValue() * 2 / (aktuelltreffend.getG11().Körper.getMass() + aktuelltreffend.getG22().Körper.getMass()));
             PhysicsObject neu = new PhysicsObject(aktuelltreffend.getG11().Körper.getMass() + aktuelltreffend.getG22().Körper.getMass(), (int) aktuelltreffend.getX(), (int) aktuelltreffend.getY(), Körper.getSim());
             neu.setV0(vNeu);//setzte v0 neu==vE
             neu.setVE(vNeu);
             ger.remove(aktuelltreffend.getG11());
             ger.remove(aktuelltreffend.getG22());
             float Defendneu = aktuelltreffend.getX() + (vNeu * (Settings.CALCULATIONS_PER_SECOND - aktuelltreffend.getT()));
             Gerade neue = new Gerade(Eresult.getY() / Eresult.getX(), aktuelltreffend.getY() - aktuelltreffend.getX() * Eresult.getY() / Eresult.getX(), aktuelltreffend.getX(), Defendneu, neu);
             neue.tanf = aktuelltreffend.getT();
             neue.gleichrichten();
             ger.add(neue);
             //verändere Körperliste
             Körper.getSim().getObjects().remove(aktuelltreffend.getG11().Körper);
             Körper.getSim().getObjects().remove(aktuelltreffend.getG22().Körper);
             Körper.getSim().getObjects().add(neu);

         }
     }




 }

public float[] Mitternacht(float a, float b, float c){
     float[]ret;
     float wurzel=(float)Math.sqrt(Math.pow (b,2)-(4*a*c));
     if(wurzel==0){
         ret=new float[1];
         ret[0]=-1*b/(2*a);
         return(ret);
     }
     ret=new float[2];
     ret[0]=(-1*b+wurzel)/(2*a);
     ret[1]=(-1*b-wurzel)/(2*a);
     return(ret);
}



}
