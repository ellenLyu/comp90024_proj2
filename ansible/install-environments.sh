#!/bin/bash

ansible-playbook install-environments.yaml --ask-become-pass -i inventory/hosts.ini