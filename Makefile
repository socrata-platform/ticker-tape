
clean:
	sbt clean

assembly:
	sbt +assembly

docker: assembly
	cp `ls -1t target/scala-2.10/*.jar | sed -n 1p` docker/ticker-tape-assembly.jar
	docker build --rm -t ticker-tape docker
