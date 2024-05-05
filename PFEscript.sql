create table administrateur(
idAd number(10) primary key,
NutilAd varchar2(30) unique,
MpAd varchar2(30));

----------------------------
create table employe(
idEmp number(10) primary key,
nomEmp varchar2(30),
prenomEmp varchar2(30),
cinEmp varchar2(20) unique,
telEmp varchar2(20) unique,
adresseEmp varchar2(80),
emailEmp varchar2(50),
sexeEmp varchar2(10),
NutilEmp varchar2(30) unique,
MpEmp varchar2(30),
idAd number(10) references administrateur(idAd)
);
------------------------------------

create table client(
idCl number(10) primary key,
nomCl varchar2(30),
prenomCl varchar2(30),
cinCl varchar2(20) unique,
telCl varchar2(20) unique,
adresseCl varchar2(80),
emailCl varchar2(50),
sexeCl varchar2(10),
idEmp number(10) references employe(idEmp)
);
-----------------------------------

create table voyage(
NumV number(10) primary key,
DateV date,
VilleDep varchar2(20),
Destination varchar2(20),
HeureDep varchar2(5),
Duree varchar2(5),
matricule varchar2(20) ,
Prix varchar2(10)
);
alter table voyage add constraint fk_matricule foreign key (matricule) references autocar(matricule) on delete cascade;
alter table voyage add (nbrPD number(3));
alter table voyage add (statut number(1));

---------------------------------------------------



create table reservation (
idCl number(10),
NumV number(10),
NumBillet number(10)
);
alter table reservation add (dateR  Date) ;
alter table reservation add constraint pk_idCl_numV_numplace primary key(idCl, NumV ,NumPlace);
alter table reservation add constraint fk_voyage foreign key (numv) references voyage(numv) on delete cascade;
alter table reservation add constraint fk_client foreign key (idcl) references client(idcl) on delete cascade;
alter table reservation add (NumPlace number(2));

-------------------------------------------------------------
create table reclamation(
NumRec number(10) primary key,
ObjetRec varchar2(60),
ContenuRec varchar2(500),
idCl number(10) references client (idCl) on delete cascade
);
alter table reclamation add (etat varchar2(20))  ;
alter table reclamation add constraint chk_etat check(etat in('traité','non traité'));
alter table reclamation add (etat varchar2(20))  ;
-------------------------------------------------
create table autocar(
matricule varchar2(20) primary key,
marque varchar2(20),
modele varchar2(20),
nbrPlaces number(3));




---------------------------------------------
create  sequence seq start with 1 increment by 1;



create or replace trigger trg_T1_Auto_Inc
 before insert on employe
 for each row
 begin
if :new.idEmp is null then
select seq.nextval into :new.idEmp from dual;
end if;
end;
-----------------------------------------------
create  sequence seq1 start with 1 increment by 1;



create or replace trigger trg_T2_Auto_Inc
 before insert on client
 for each row
 begin
if :new.idCl is null then
select seq1.nextval into :new.idCl from dual;
end if;
end;

--------------------------------------------------
create  sequence seq2 start with 1 increment by 1;



create or replace trigger trg_T3_Auto_Inc
 before insert on voyage
 for each row
 begin
if :new.NumV is null then
select seq2.nextval into :new.NumV from dual;
end if;
end;



----------------------------------------
create  sequence seq4 start with 1 increment by 3;




create or replace trigger trg_T5_Auto_Inc
 before insert on reservation
 for each row
 begin
if :new.NumBillet is null then
select seq4.nextval into :new.NumBillet from dual;
end if;
end;


--------------------------------------------

create  sequence seq5 start with 1 increment by 1;



create or replace trigger trg_T6_Auto_Inc
 before insert on reclamation
 for each row
 begin
if :new.numrec is null then
select seq5.nextval into :new.numrec from dual;
end if;
end;
---------------------------------
--insertion num place , la mise à  jours nombre de places disponible et le statut du voyage
create or replace trigger insert_numP
 before insert on reservation
 for each row
 declare
  nbrp number(2);
  nbr_pd number(2);
  
 begin

  select nbrplaces into nbrp from autocar 
  where matricule = (select matricule from voyage where numv = :new.numv ) ;
  
  select nbrpd into nbr_pd from voyage where numv = :new.numv;
  
if nbr_pd<1 then

  update voyage set statut=0 where numv =:new.numv;
  
  RAISE_APPLICATION_ERROR(-20001, 'Autocar plein');
end if;
 
if :new.NumPlace is null then
  select (nbrp-nbr_pd+1) into :new.NumPlace from dual;
  update voyage set nbrpd = nbrpd-1 where numv = :new.numv;
  
end if;

if nbr_pd<=1 then
  update voyage set statut=0 where numv =:new.numv;
  
end if;
end;

---------------------------
--insert nombre de place into nombre de places disponible
create or replace trigger voyage_nbrpd
 before insert on voyage
 for each row

 begin
  
if :new.nbrpd is null then
select nbrplaces into  :new.nbrpd from autocar where matricule = :new.matricule;
end if;
end;








--verification num place < nombre de place
create or replace trigger Constraint_nbrPlc_
 before insert on reservation
 for each row
 declare 
  nbr_pd number(2);
 

 begin
select nbrpd into nbr_pd from voyage where numv = :new.numv;

if nbr_pd<1 then
 update voyage set statut=1 where numv =:new.numv;
RAISE_APPLICATION_ERROR(-20001, 'Autocar plein');
end if;
end;


-------------------------------------------
