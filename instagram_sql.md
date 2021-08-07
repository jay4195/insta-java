# Instagram Clone  MySQL

## 1 User Table

```mysql
create table if not exists user (
	id bigint not null auto_increment primary key,
    username varchar(25),
    password varchar(25),
    fullname varchar(50),
    email varchar(50),
    avatar varchar(50),
    website varchar(50),
    bio varchar(100),
    createdAt timestamp not null
);

create table if not exists user_follow (
	uid bigint not null,
    follower_uid bigint not null,
    foreign key (uid) references user(id) on delete cascade,
    foreign key (follower_uid) references user(id) on delete cascade
);
```

## 2 Post Table

```mysql
create table if not exists post (
	id bigint not null auto_increment primary key,
    uid bigint,
    caption varchar(150),
    createdAt timestamp not null,
    foreign key (uid) references user(id) on delete cascade
);

create table if not exists post_images (
	post_id bigint,
    image_url varchar(50) not null,
    foreign key (post_id) references post(id) on delete cascade
);

create table if not exists post_tags (
	post_id bigint,
    hashtag varchar(20) not null,
    foreign key (post_id) references post(id) on delete cascade
);

create table if not exists post_comments (
    post_id bigint,
    uid bigint,
    comment varchar(150),
    createdAt timestamp not null,
    foreign key (post_id) references post(id) on delete cascade,
    foreign key (uid) references user(id) on delete cascade
);

create table if not exists post_likes (
	post_id bigint,
    uid bigint,
    foreign key (post_id) references post(id) on delete cascade,
    foreign key (uid) references user(id) on delete cascade
);

create table if not exists post_saves (
	post_id bigint,
    uid bigint,
    foreign key (post_id) references post(id) on delete cascade,
    foreign key (uid) 
    references user(id) on delete cascade
);
```

## 3. Message sql

```sql
create table if not exists message (
	sender_id bigint not null,
    receiver_id bigint not null,
    text text not null,
    createdAt timestamp not null,
    foreign key (sender_id) references user(id) on delete cascade,
    foreign key (receiver_id) references user(id) on delete cascade
);
```

