--select count(*) from testecarga.localizacoes where imei = '205275572'

--select json_agg(t) from testecarga.localizacoes t where imei = '007479784';

--select json_build_object(t.id, t.loguser, t.date, t.speed) as json from testecarga.localizacoes t where imei = '007479784';

--select json_agg(json_build_object(t.id, t.speed)) from testecarga.localizacoes t where imei = '007479784';

--SELECT to_json((SELECT d FROM (SELECT id, speed, latitude, longitude) d)) AS data FROM testecarga.localizacoes where imei = '007479784';

--SELECT json_agg(to_json((SELECT d FROM (SELECT id, speed, latitude, longitude) d))) AS data FROM testecarga.localizacoes where imei = '007479784';

--select json_agg(t) from testecarga.localizacoes t where imei = '007479784';

--SELECT array_to_json(array_agg(t)) FROM testecarga.localizacoes t where imei = '007479784';



SELECT json_agg(to_json((SELECT d FROM (SELECT id, speed, latitude, longitude) d))) AS data FROM testecarga.localizacoes where imei in ('205275572', '007479784', '007523105');

-- select json_agg(to_json((select d from (select din, dout, peer, id) d))) as data from (select din, dout, peer, id from comigo.localizacoes where licenseplate='QXC-1B15') l
-- select json_agg(to_json((select d from (select din, dout, peer, id) d))) as data from (select din, dout, peer, id from comigo.localizacoes where licenseplate='QXC-1B15') l 
   select json_agg(to_json((select d from (select din, dout, peer, id) d order by id asc limit 1000 offset 0))) as data from comigo.localizacoes l where licenseplate='QXC-1B15'



>>> delay [on browser] [registers 1871/Response] [0.8MB/Req] [delay: 1781ms] [avg: 2226.41095890411ms] [avg: 1.12ms]

>>> latency [on persistence-d-c] [/s/no-ac]                         [now 254ms] [avg: 369ms] [3.7% CPU]
>>> latency [on dbr-si-driver]   [/unsecured/schema/select/as-json] [now 292ms] [avg: 326ms] [3.4% CPU]




>>> delay [on browser] [registers 5878/Response] [2.6MB/Req] [delay: 1995ms] [avg: 3249.721804511279ms] [avg: 2.10ms]

>>> latency [on persistence-d-c] [/s/no-ac]                         [now 353ms] [avg: 427ms] [6.3% CPU]
>>> latency [on dbr-si-driver]   [/unsecured/schema/select/as-json] [now 406ms] [avg: 344ms] [5.7% CPU]


