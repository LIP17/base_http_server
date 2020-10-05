.PHONY: lint-check
lint-check:
	./gradlew ktlintCheck

.PHONY: lint-fix
lint-fix:
	./gradlew ktlintFormat
