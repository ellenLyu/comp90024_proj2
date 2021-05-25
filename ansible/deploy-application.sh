#!/bin/bash

ansible-playbook deploy-application.yaml --ask-become-pass -i inventory/hosts.ini