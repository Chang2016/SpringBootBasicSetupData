

create table if not exists course (id bigint not null auto_increment, name varchar(255), start_date date, topic_id bigint, primary key (id)) engine=MyISAM;
create table if not exists course_student (course_id bigint not null, student_id bigint not null, primary key (course_id, student_id)) engine=MyISAM;
create table if not exists student (id bigint not null auto_increment, birthday date, created datetime, name varchar(255) not null, primary key (id)) engine=MyISAM;
create table if not exists topic (id bigint not null auto_increment, name varchar(255), primary key (id)) engine=MyISAM;
alter table course add constraint FKokaxyfpv8p583w8yspapfb2ar foreign key (topic_id) references topic (id);
alter table course_student add constraint FK4xxxkt1m6afc9vxp3ryb0xfhi foreign key (student_id) references student (id);
alter table course_student add constraint FKlmj50qx9k98b7li5li74nnylb foreign key (course_id) references course (id);
