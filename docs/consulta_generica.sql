select f.disciplina, avg(nota) as media_avaliacoes, u.email, u.nome from feedback f
inner join usuario u on u.id = f.usuario_professor
group by disciplina, usuario_professor

update feedback set disciplina ='Serveless' where id=5
update feedback set usuario_professor =8 where usuario_professor is null

update feedback set nota =8 where id=3

select * from usuario
select * from feedback

update usuario set email='schn.alessandro@gmail.com' where login = 'admin'

insert into usuario(email,login,nome,senha)
values ("schn.alessandro@gmail.com","professor01","Professor de Algoritmos","$2a$12$af/fW53EX3KTxg3zTax6KOcVHATo50CV7MZClhwLY63eRkh7/Vnka")