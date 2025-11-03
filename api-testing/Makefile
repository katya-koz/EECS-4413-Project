venv:
	python3 -m venv venv
	. venv/bin/activate && pip3 install -r requirements.txt

test: venv
	. venv/bin/activate && python3 unit_tests.py
.PHONY: test

clean:
	-rm -r venv
.PHONY: clean
