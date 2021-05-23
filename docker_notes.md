# notes
docker root: /var/lib/docker


# proxy
/etc/systemd/system/docker.service.d/proxy.conf
[Service]
Environment="HTTP_PROXY=http://wwwproxy.unimelb.edu.au:8000/"
Environment="HTTPS_PROXY=http://wwwproxy.unimelb.edu.au:8000/"
Environment="http_proxy=http://wwwproxy.unimelb.edu.au:8000/"
Environment="https_proxy=http://wwwproxy.unimelb.edu.au:8000/"
Environment="no_proxy=localhost,127.0.0.1,localaddress,172.16.0.0/12,.melbourne.rc.nectar.org.au,.storage.unimelb.edu.au,.cloud.unimelb.edu.au"

## restart docker 
sudo systemctl daemon-reload
sudo systemctl restart docker.service


# docker swarm

## security group 
security group add inbound rules: tcp 2377, tec/udp 7946, udp 4789, tcp 5984
* couch db 是不是还要9100， 4369

sudo docker swarm init 
sudo docker swarm join-token --quiet worker // get worker join token 

⬇️ worker join
sudo docker swarm join --token SWMTKN-1-0rn644v663t89xgvyjd7fo7cnsjylcejp6hfsqiab2xr219twt-c7tzos6uba634fncorj1f6yva 172.26.128.60:2377

## notes
prepare couchdb-conf.ini & docker-swarm.yaml

## deploy
master node run:
sudo docker stack deploy -c docker-swarm.yaml test

curl -X POST -H "Content-Type: application/json" http://admin:group27@127.0.0.1:5984/_cluster_setup -d '{"action": "enable_cluster", "bind_address":"0.0.0.0", "username": "admin", "password":"group27", "node_count":"3"}'

⬇️vdjm9ljjmsxavu8679lx2k8t3 => docker node uuid
⬇️xkhf4iw2hfqtt8e6ylkjtlpa2 => couch db uuid
curl -X PUT "http://admin:group27@127.0.0.1:5984/_node/_local/_nodes/couchdb@test_couchdb.vdjm9ljjmsxavu8679lx2k8t3.xkhf4iw2hfqtt8e6ylkjtlpa2" -d {}

curl -X PUT "http://admin:group27@127.0.0.1:5984/_node/_local/_nodes/couchdb@test_couchdb.mg9w78qlzopk8tliskgxw8292.by6aduo8b6988esf98kiv6o49" -d {}

curl -X POST -H "Content-Type: application/json" http://admin:group27@127.0.0.1:5984/_cluster_setup -d '{"action": "finish_cluster"}'


curl http://admin:group27@127.0.0.1:5984/_membership

curl -X PUT http://admin:group27@127.0.0.1:5984/test_database

curl -X GET http://admin:group27@127.0.0.1:5984/_all_dbs

curl -X PUT "http://admin:group27@127.0.0.1:5984/test/01" -d '{"key": "val"}'

curl "http://admin:group27@127.0.0.1:5984/test/01"

# pre-requisite
security group add inbound rules: tcp 2377, 7946

sudo docker swarm init 
sudo docker swarm join-token --quiet worker // get worker join token 

sudo docker swarm join --token SWMTKN-1-0rn644v663t89xgvyjd7fo7cnsjylcejp6hfsqiab2xr219twt-c7tzos6uba634fncorj1f6yva 172.26.128.60:2377



sudo docker stack ps test


# ref

pw
https://sleeplessbeastie.eu/2020/03/13/how-to-generate-password-hash-for-couchdb-administrator/

nginx load balancer
https://gist.github.com/itasyurt/df4b77b8d013f82327f02b57ca7b8f2a


docker
https://docs.docker.com/engine/install/ubuntu/

swarm
https://ssarcandy.tw/2021/01/26/setup-couchdb-using-docker-swarm/
https://www.cnblogs.com/zhujingzhi/p/9792432.html

nginx+springboot
https://medium.com/dev-genius/load-balancing-a-spring-boot-application-with-nginx-and-docker-e701f74c011d
PASS="group27" SALT="8a3bfe04b1f4294d89d9e9d250fce77a" ITER=10 \
  python3 -c "import os,hashlib; print('-pbkdf2-%s,%s,%s' % (hashlib.pbkdf2_hmac('sha1',os.environ['PASS'].encode(),os.environ['SALT'].encode(),int(os.environ['ITER'].encode())).hex(), os.environ['SALT'], os.environ['ITER']))"

  

Source: https://ssarcandy.tw/2021/01/26/setup-couchdb-using-docker-swarm/
