oc logs --follow $(oc get pods --output jsonpath='{.items[?(.metadata.labels.name=="mongodb")].metadata.name}')
