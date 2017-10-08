oc rsh $(oc get pods --output jsonpath='{.items[?(.metadata.labels.name=="mongodb")].metadata.name}')
