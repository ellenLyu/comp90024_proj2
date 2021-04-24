#!/bin/bash

. ./grp-27-openrc.sh; ansible-playbook install-environments.yaml --ask-become-pass -i inventory/hosts.ini