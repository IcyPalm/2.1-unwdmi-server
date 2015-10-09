# Commands:
# Build source into class files in the `bin` directory:
# > make build
# (Is also the default.)
# > make
# Build & Run program:
# > make run
# Generate documentation:
# > make doc

all: build

# Building source
build:
	mkdir -p bin
	javac -d bin -sourcepath src src/weatherhandler/ServerStarter.java

# Ensures stuff is built before running it
run: build
	# TODO don't hardcode this, dummy
	# I don't really know how java classpath config works, it seems really shit
	# to have to manually set this as a user though >_>
	./run

opendb:
	psql weatherhandler -h 127.0.0.1 -d weatherhandler

# Generates documentation
doc:
	javadoc -d ./doc -encoding utf8 weatherhandler src/weatherhandler/**/*
