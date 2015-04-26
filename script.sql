#La conexió amb hibernate esta feta amb l'usuari examen amb contrasenya examen, a continuació esta la comanda per crear-lo i assignar-li els permisos

#create USER 'examen'@'localhost' identified by 'examen';
#grant all privileges on Examen.* to 'examen'@'localhost';


drop database if exists Examen;
create database Examen;

use Examen;

create table Event(
	id int PRIMARY KEY auto_increment,
	name varchar(20),
	description varchar(100),
	price double(7,2),
	ticketsAvailable boolean,
	data date
) engine = InnoDB;

insert into Event (name, description, price, ticketsAvailable, data) values ("Event1", "event 1", 29.50, true, "2015-10-20");
insert into Event (name, description, price, ticketsAvailable, data) values ("Event2", "event 2", 30.50, false, "2015-09-30");
insert into Event (name, description, price, ticketsAvailable, data) values ("Event3", "event 3", 10.99, true, "2016-01-25");

select * from Event;


