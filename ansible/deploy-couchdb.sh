#!/bin/bash

ansible-playbook deploy-couchdb.yaml --ask-become-pass -i inventory/couchdb-cluster.ini