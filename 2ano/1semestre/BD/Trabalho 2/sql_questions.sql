--a)
SELECT DISTINCT local
FROM animals
NATURAL INNER JOIN class_bio
WHERE class like 'aves';

A5.2

--b)
SELECT DISTINCT local 
from animals
natural INNER JOIN class_bio
WHERE orderA not like 'carnívoros';

A1 
A2 
A5.2

--c)
SELECT nameA
from animals
natural INNER JOIN captivity
WHERE registermom = (SELECT registermom 
  FROM captivity
  NATURAL INNER JOIN animals
  WHERE animals.nameA LIKE 'Kilu')
  or
registerdad = (
  SELECT registerdad 
  FROM captivity
  NATURAL INNER JOIN animals
  WHERE animals.nameA LIKE 'Kilu')
EXCEPT
SELECT nameA 
FROM animals
WHERE nameA LIKE 'Kilu';

Kuli

--d)
SELECT numbertele
FROM telephone
NATURAL INNER JOIN keeper
NATURAL INNER JOIN animals
WHERE animals.name LIKE 'Kata';

919999999 
266787809

--e)
WITH teleN as (select nifE 
from aux_keeper
NATURAL INNER JOIN animals
where animals.name LIKE 'Kata' AND animals.local = aux_keeper.local)
select numbertele 
from telephone
natural inner JOIN teleN
Natural INNER JOIN responsible
where teleN.nif = responsible.niftrab;

919999996 
266787806

--f)
SELECT data, diagnosis
FROM consultations
NATURAL INNER JOIN animals
WHERE animals.name LIKE 'Mali';

2005-08-12 grávida
2005-09-12 cálcio injetado
2005-12-12 parto
2006-07-12 infeção
2006-07-12 antibiótico injetado

--g)
SELECT DISTINCT name 
FROM employees
NATURAL INNER JOIN consultations
NATURAL INNER JOIN class_bio
WHERE consultations.nif = employees.nif AND consultations.register = class_bio.register 
AND class_bio.ordera LIKE 'carnívoros';

Pedro Vale

--h)
SELECT family, COUNT(ordera) AS animalsN
FROM class_bio
WHERE ordera LIKE 'artiodáctilos'
GROUP BY family;

cervídeos 5
hipopótamos 3

--j)
SELECT ordera, COUNT(ordera) AS orderN
FROM class_bio
GROUP by ordera
Order by orderN DESC
LIMIT 1;

artiodáctilos 8

--k)
SELECT ordera, count(ordera) 
from class_bio
natural INNER join consultations
where class_bio.register = consultations.register
group by ordera 
HAVING COUNT(ordera) > 5;

artiodáctilos 12
psittaciformes 6

--l)
WITH animalsN as (SELECT register, count(register) as captive 
from captivity
group by register)
select SUM(captive) from animalsN;

10

--m)
WITH oldA AS (SELECT datan AS age , register
FROM captivity
UNION
SELECT datan AS age , register
FROM capture)
SELECT name FROM animals
WHERE register =
(SELECT register
FROM oldA
WHERE age = (SELECT MIN(age)
FROM oldA));

Mali

--n)

--o)
WITH mammals AS (SELECT register FROM animals
NATURAL INNER JOIN class_bio
WHERE class LIKE 'mamíferos'),
mammalsPerKeeper AS (SELECT nif, register 
FROM keeper 
NATURAL inner join mammals)
SELECT name, COUNT(mammalsPerKeeper.nif)
AS mammalkeeper
FROM employees 
JOIN mammalsPerKeeper
ON employees.nif = mammalsPerKeeper.nif
GROUP BY name;

Joaquim Silva 7
Manuel Santos 8

--p)
WITH vetIsabel as (Select registera from consultations
NATURAL INNER JOIN veterinary
NATURAL INNER JOIN employees
WHERE employees.namee LIKE 'Isabel Soares' )
vetPedro as (Select registera from consultations
NATURAL INNER JOIN veterinary
NATURAL INNER JOIN employees
WHERE employees.namee LIKE 'Pedro Vale'),
select DISTINCT namea from animals 
Natural inner join vetPedro
NATURAL inner join vetIsabel;

Tapi
Zula
