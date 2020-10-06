.PHONY: lint-check
lint-check:
	./gradlew ktlintCheck

.PHONY: lint-fix
lint-fix:
	./gradlew ktlintFormat

# TODO remove the 0.1 tag to be programmable
.PHONY: build-image
build-image:
	docker build -t lip17/base-http-server:0.1 -t lip17/base-http-server:latest .

# TODO: make port configurable
.PHONY: run-docker
run-docker:
	docker run -e SERVER_PORT=8080 -p 8080:8080 lip17/base-http-server:latest
