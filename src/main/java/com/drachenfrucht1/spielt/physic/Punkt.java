package com.drachenfrucht1.spielt.physic;
import lombok.Getter;
import lombok.Setter;
public class Punkt {
    private @Getter @Setter float x;
    private @Getter @Setter float y;
    private @Getter @Setter float t;
    private @Getter @Setter Gerade g11;
    private @Getter @Setter Gerade g22;
    Punkt(float x, float y,float t ){
        this.x=x;
        this.y=y;
        this.t=t;


    }

    public void Geradenschneiden(Gerade g1, Gerade g2){
        float max;
        float min;
        if(g1.Defanf<g2.Defanf){
            min=g2.Defanf;
        }
        else{
            min=g1.Defanf;
        }
        if(g2.Defend<g2.Defend){
            max=g2.Defend;
        }
        else{
            max=g1.Defend;
        }
        if((g1.c-g2.c)/(g2.m-g1.m)<=max&&(g1.c-g2.c)/(g2.m-g1.m)>=min){
             //treffer ja, aber zur gleichen Zeit?
            //treffer an Pkt:

            setX((g1.c-g2.c)/(g2.m-g1.m));
            setY(g2.m*(g1.c-g2.c)/(g2.m-g1.m)+g2.c);
            float v1=(g1.Körper.getVE()+g1.Körper.getV0())/2;//komplette v Durchschnitt--> eig ungenau für best Abshcnitt
            Vector2D abst=new Vector2D(g1.Körper.getAnfangspunkt().getX()-x,g1.Körper.getAnfangspunkt().getY()-y);
            float t1=abst.getValue()/v1+g1.tanf;

            float v2=(g2.Körper.getVE()+g2.Körper.getV0())/2;//komplette v Durchschnitt--> eig ungenau für best Abshcnitt
            Vector2D abst2=new Vector2D(g2.Körper.getAnfangspunkt().getX()-x,g2.Körper.getAnfangspunkt().getY()-y);
            float t2=abst2.getValue()/v2+g2.tanf;

            t1=runden(t1);
            t2=runden(t2);
            if(t1==t2){
                setT(t1);
                setG11(g1);
                setG22(g2);
                g1.Körper.setVE((float)v1);//eig Gesamtdurchschnitt
                g2.Körper.setVE((float)v2);
            }
            else{

            }


        }

    }
    public float runden(float r){//schriebe richtige Methode
        String f=r+"";
        String[]teile=f.split(".");
        if(teile[1].length()<3){
            return(r);
        }
        else{
            char c=teile[1].charAt(2);
            String c1=""+c;
            if(Integer.parseInt(c1)<5){//abrunden
                String neu=teile[0]+"."+teile[1].charAt(0)+teile[1].charAt(1);
                return(Float.parseFloat(neu));


            }
            else{
                String neu=teile[0]+"."+teile[1].charAt(0)+teile[1].charAt(1);
                return(Float.parseFloat(neu)+(float)0.001);

            }
        }

    }
    public float rundenachkomma(float r, int stell){//stell enstpricht stelle nach komma, auf die gerundet werden soll
        String f=r+"";
        String[]teile=f.split(".");
        if(teile[1].length()-1<stell){
            return(r);
        }
        String c=""+teile[1].charAt(stell);
        String neu=teile[0]+".";
        String add="0."
        for(int i=0;i<stell;i++){
            neu=neu+""+teile[1].charAt(i);
        }
        for(int i=1;i<stell;i++){
            add=add+"0";
        }
        add=add+"1";
        float ad=Float.parseFloat(add);

        if(Integer.parseInt(c)<5){
            return(Float.parseFloat(neu));


        }
        else{
            return(Float.parseFloat(neu)+ad);
        }


    }
}
