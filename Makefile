.PHONY: test install clean

install:
	mvn install

test:
	mvn test

clean:
	rm -rf target
