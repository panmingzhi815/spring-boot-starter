create table system_account
(
	id bigint not null auto_increment
		primary key,
	username varchar(45) not null,
	password varchar(150) not null,
	constraint SystemAccount_UNIQUE
		unique (username)
)
;

create table system_account_role
(
	system_role bigint null,
	system_account bigint null
)
;

create table system_resource
(
	id bigint not null auto_increment
		primary key,
	resourceName varchar(50) not null,
	resourceUrl varchar(300) null,
	resourceIcon varchar(50) null,
	resourceIndex int default '0' null,
	resourceParent bigint null,
	constraint system_resource_system_resource_id_fk
		foreign key (resourceParent) references test.system_resource (id)
)
;

create index system_resource_system_resource_id_fk
	on system_resource (resourceParent)
;

create table system_role
(
	id bigint not null auto_increment
		primary key,
	roleName varchar(30) not null,
	constraint SystemRole_roleName_uindex
		unique (roleName)
)
;

create table system_role_resource
(
	role_id bigint not null,
	resource_id bigint not null,
	primary key (role_id, resource_id)
)
;

