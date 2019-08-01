create view WF_JOBS_VW as
select t1.id,t1.app_name,t1.app_path,t1.user_name,t1.start_time,t1.end_time,t2.nominal_time,t1.status,TIMESTAMPDIFF(SECOND,t1.start_time,t1.end_time) dur_second_time
from WF_JOBS t1
left join COORD_ACTIONS t2
on t1.id=t2.external_id;
