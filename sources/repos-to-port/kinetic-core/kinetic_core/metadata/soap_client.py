"""
SOAP client for Salesforce Metadata API.

Handles low-level SOAP communication with Salesforce Metadata API.
"""

import time
import base64
from typing import Dict, List, Any, Optional
from xml.etree import ElementTree as ET

import requests

from kinetic_core.core.session import SalesforceSession


# Salesforce SOAP namespaces
SOAP_NS = "http://schemas.xmlsoap.org/soap/envelope/"
METADATA_NS = "http://soap.sforce.com/2006/04/metadata"


class MetadataSOAPClient:
    """
    Low-level SOAP client for Salesforce Metadata API.

    Handles SOAP envelope creation, requests, and response parsing.
    """

    def __init__(self, session: SalesforceSession):
        """
        Initialize SOAP client.

        Args:
            session: Authenticated Salesforce session
        """
        self.session = session
        # Remove 'v' prefix from api_version for Metadata API endpoint
        # (e.g., v62.0 -> 62.0)
        api_version_num = session.api_version.lstrip('v')
        self.endpoint = f"{session.instance_url}/services/Soap/m/{api_version_num}"

    def _create_soap_envelope(self, body_content: str) -> str:
        """
        Create SOAP envelope with session header.

        Args:
            body_content: XML content for SOAP body

        Returns:
            Complete SOAP envelope as string
        """
        envelope = f"""<?xml version="1.0" encoding="utf-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:met="http://soap.sforce.com/2006/04/metadata">
  <soapenv:Header>
    <met:SessionHeader>
      <met:sessionId>{self.session.access_token}</met:sessionId>
    </met:SessionHeader>
  </soapenv:Header>
  <soapenv:Body>
    {body_content}
  </soapenv:Body>
</soapenv:Envelope>"""
        return envelope

    def _send_request(self, soap_envelope: str) -> ET.Element:
        """
        Send SOAP request to Salesforce.

        Args:
            soap_envelope: Complete SOAP envelope

        Returns:
            Parsed XML response body

        Raises:
            requests.HTTPError: If request fails
            RuntimeError: If SOAP fault received
        """
        headers = {
            "Content-Type": "text/xml; charset=utf-8",
            "SOAPAction": '""',
        }

        response = requests.post(
            self.endpoint,
            data=soap_envelope.encode("utf-8"),
            headers=headers,
            timeout=120,
        )

        # Parse response
        root = ET.fromstring(response.content)

        # Check for SOAP fault
        fault = root.find(".//{http://schemas.xmlsoap.org/soap/envelope/}Fault")
        if fault is not None:
            fault_code = fault.findtext("faultcode", "Unknown")
            fault_string = fault.findtext("faultstring", "Unknown error")
            raise RuntimeError(f"SOAP Fault [{fault_code}]: {fault_string}")

        # Extract body
        body = root.find(".//{http://schemas.xmlsoap.org/soap/envelope/}Body")
        if body is None:
            raise RuntimeError("No SOAP body in response")

        return body

    def describe_metadata(self, api_version: Optional[str] = None) -> Dict[str, Any]:
        """
        Call describeMetadata SOAP operation.

        Args:
            api_version: API version (defaults to session version)

        Returns:
            Dictionary with metadata types information
        """
        version = api_version or self.session.api_version
        # Remove 'v' prefix for SOAP body (expects numeric version)
        version_num = version.lstrip('v')

        body_content = f"""
    <met:describeMetadata>
      <met:asOfVersion>{version_num}</met:asOfVersion>
    </met:describeMetadata>"""

        envelope = self._create_soap_envelope(body_content)
        response_body = self._send_request(envelope)

        # Parse result
        result_elem = response_body.find(
            ".//{http://soap.sforce.com/2006/04/metadata}result"
        )

        if result_elem is None:
            return {"metadataObjects": []}

        # Extract metadata types
        metadata_objects = []
        for obj_elem in result_elem.findall(
            "{http://soap.sforce.com/2006/04/metadata}metadataObjects"
        ):
            obj_info = {
                "directoryName": obj_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}directoryName", ""
                ),
                "xmlName": obj_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}xmlName", ""
                ),
                "suffix": obj_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}suffix"
                ),
            }
            metadata_objects.append(obj_info)

        return {
            "organizationNamespace": result_elem.findtext(
                "{http://soap.sforce.com/2006/04/metadata}organizationNamespace", ""
            ),
            "partialSaveAllowed": result_elem.findtext(
                "{http://soap.sforce.com/2006/04/metadata}partialSaveAllowed", "false"
            )
            == "true",
            "testRequired": result_elem.findtext(
                "{http://soap.sforce.com/2006/04/metadata}testRequired", "false"
            )
            == "true",
            "metadataObjects": metadata_objects,
        }

    def retrieve(
        self, package_xml: str, api_version: Optional[str] = None
    ) -> str:
        """
        Call retrieve SOAP operation.

        Args:
            package_xml: package.xml content
            api_version: API version (defaults to session version)

        Returns:
            Async retrieve request ID
        """
        version = api_version or self.session.api_version
        # Remove 'v' prefix for SOAP body (expects numeric version)
        version_num = version.lstrip('v')

        # Encode package.xml as base64
        package_b64 = base64.b64encode(package_xml.encode("utf-8")).decode("ascii")

        body_content = f"""
    <met:retrieve>
      <met:retrieveRequest>
        <met:apiVersion>{version_num}</met:apiVersion>
        <met:unpackaged>{package_b64}</met:unpackaged>
      </met:retrieveRequest>
    </met:retrieve>"""

        envelope = self._create_soap_envelope(body_content)
        response_body = self._send_request(envelope)

        # Extract async ID
        result_elem = response_body.find(
            ".//{http://soap.sforce.com/2006/04/metadata}result"
        )
        if result_elem is None:
            raise RuntimeError("No result in retrieve response")

        async_id_elem = result_elem.find(
            "{http://soap.sforce.com/2006/04/metadata}id"
        )
        if async_id_elem is None or not async_id_elem.text:
            raise RuntimeError("No ID in retrieve result")

        return async_id_elem.text

    def check_retrieve_status(self, async_id: str) -> Dict[str, Any]:
        """
        Check status of retrieve operation.

        Args:
            async_id: Async process ID from retrieve()

        Returns:
            Status dictionary with done, success, and zipFile (if done)
        """
        body_content = f"""
    <met:checkRetrieveStatus>
      <met:asyncProcessId>{async_id}</met:asyncProcessId>
    </met:checkRetrieveStatus>"""

        envelope = self._create_soap_envelope(body_content)
        response_body = self._send_request(envelope)

        # Parse result
        result_elem = response_body.find(
            ".//{http://soap.sforce.com/2006/04/metadata}result"
        )
        if result_elem is None:
            return {"done": False, "success": False}

        done = (
            result_elem.findtext("{http://soap.sforce.com/2006/04/metadata}done", "false")
            == "true"
        )
        success = (
            result_elem.findtext(
                "{http://soap.sforce.com/2006/04/metadata}success", "false"
            )
            == "true"
        )
        status = result_elem.findtext(
            "{http://soap.sforce.com/2006/04/metadata}status", "Unknown"
        )

        result = {"done": done, "success": success, "status": status}

        # If done and successful, extract ZIP
        if done and success:
            zip_file_elem = result_elem.find(
                "{http://soap.sforce.com/2006/04/metadata}zipFile"
            )
            if zip_file_elem is not None and zip_file_elem.text:
                result["zipFile"] = base64.b64decode(zip_file_elem.text)

        # Extract file properties
        file_properties = []
        for prop_elem in result_elem.findall(
            "{http://soap.sforce.com/2006/04/metadata}fileProperties"
        ):
            file_info = {
                "fileName": prop_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}fileName", ""
                ),
                "fullName": prop_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}fullName", ""
                ),
                "type": prop_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}type", ""
                ),
            }
            file_properties.append(file_info)

        result["fileProperties"] = file_properties

        # Extract messages (errors/warnings)
        messages = []
        for msg_elem in result_elem.findall(
            "{http://soap.sforce.com/2006/04/metadata}messages"
        ):
            msg = msg_elem.findtext(
                "{http://soap.sforce.com/2006/04/metadata}message", ""
            )
            if msg:
                messages.append(msg)

        result["messages"] = messages

        return result

    def deploy(
        self,
        zip_file: bytes,
        check_only: bool = False,
        run_tests: bool = False,
        rollback_on_error: bool = True,
    ) -> str:
        """
        Call deploy SOAP operation.

        Args:
            zip_file: ZIP file content (bytes)
            check_only: Validation only (no actual deployment)
            run_tests: Run local tests during deployment (default: False)
            rollback_on_error: Rollback all changes on error

        Returns:
            Async deploy request ID
        """
        # Encode ZIP as base64
        zip_b64 = base64.b64encode(zip_file).decode("ascii")

        body_content = f"""
    <met:deploy>
      <met:ZipFile>{zip_b64}</met:ZipFile>
      <met:DeployOptions>
        <met:checkOnly>{str(check_only).lower()}</met:checkOnly>
        <met:rollbackOnError>{str(rollback_on_error).lower()}</met:rollbackOnError>
        <met:testLevel>{'RunLocalTests' if run_tests else 'NoTestRun'}</met:testLevel>
        <met:singlePackage>true</met:singlePackage>
      </met:DeployOptions>
    </met:deploy>"""

        envelope = self._create_soap_envelope(body_content)
        response_body = self._send_request(envelope)

        # Extract async ID
        result_elem = response_body.find(
            ".//{http://soap.sforce.com/2006/04/metadata}result"
        )
        if result_elem is None:
            raise RuntimeError("No result in deploy response")

        async_id_elem = result_elem.find("{http://soap.sforce.com/2006/04/metadata}id")
        if async_id_elem is None or not async_id_elem.text:
            raise RuntimeError("No ID in deploy result")

        return async_id_elem.text

    def check_deploy_status(self, async_id: str) -> Dict[str, Any]:
        """
        Check status of deploy operation.

        Args:
            async_id: Async process ID from deploy()

        Returns:
            Status dictionary with done, success, and component details
        """
        body_content = f"""
    <met:checkDeployStatus>
      <met:asyncProcessId>{async_id}</met:asyncProcessId>
    </met:checkDeployStatus>"""

        envelope = self._create_soap_envelope(body_content)
        response_body = self._send_request(envelope)

        # Parse result
        result_elem = response_body.find(
            ".//{http://soap.sforce.com/2006/04/metadata}result"
        )
        if result_elem is None:
            return {"done": False, "success": False}

        done = (
            result_elem.findtext("{http://soap.sforce.com/2006/04/metadata}done", "false")
            == "true"
        )
        success = (
            result_elem.findtext(
                "{http://soap.sforce.com/2006/04/metadata}success", "false"
            )
            == "true"
        )
        status = result_elem.findtext(
            "{http://soap.sforce.com/2006/04/metadata}status", "Unknown"
        )

        result = {
            "done": done,
            "success": success,
            "status": status,
            "componentSuccesses": [],
            "componentFailures": [],
        }

        # Extract component successes
        for comp_elem in result_elem.findall(
            "{http://soap.sforce.com/2006/04/metadata}details/"
            "{http://soap.sforce.com/2006/04/metadata}componentSuccesses"
        ):
            comp_info = {
                "fileName": comp_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}fileName", ""
                ),
                "fullName": comp_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}fullName", ""
                ),
            }
            result["componentSuccesses"].append(comp_info)

        # Extract component failures
        for comp_elem in result_elem.findall(
            "{http://soap.sforce.com/2006/04/metadata}details/"
            "{http://soap.sforce.com/2006/04/metadata}componentFailures"
        ):
            comp_info = {
                "fileName": comp_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}fileName", ""
                ),
                "fullName": comp_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}fullName", ""
                ),
                "problem": comp_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}problem", ""
                ),
                "problemType": comp_elem.findtext(
                    "{http://soap.sforce.com/2006/04/metadata}problemType", ""
                ),
            }
            result["componentFailures"].append(comp_info)

        # Extract top-level error message
        error_message = result_elem.findtext(
            "{http://soap.sforce.com/2006/04/metadata}errorMessage"
        )
        if error_message:
            result["messages"].append(f"Global Error: {error_message}")

        return result

    def wait_for_retrieve(
        self, async_id: str, timeout: int = 300, poll_interval: int = 5
    ) -> Dict[str, Any]:
        """
        Wait for retrieve operation to complete.

        Args:
            async_id: Async process ID from retrieve()
            timeout: Maximum wait time in seconds
            poll_interval: Seconds between status checks

        Returns:
            Final status dictionary

        Raises:
            TimeoutError: If operation doesn't complete within timeout
        """
        start_time = time.time()

        while True:
            status = self.check_retrieve_status(async_id)

            if status["done"]:
                return status

            # Check timeout
            if time.time() - start_time > timeout:
                raise TimeoutError(
                    f"Retrieve operation {async_id} did not complete within {timeout}s"
                )

            # Wait before next poll
            time.sleep(poll_interval)

    def wait_for_deploy(
        self, async_id: str, timeout: int = 300, poll_interval: int = 5
    ) -> Dict[str, Any]:
        """
        Wait for deploy operation to complete.

        Args:
            async_id: Async process ID from deploy()
            timeout: Maximum wait time in seconds
            poll_interval: Seconds between status checks

        Returns:
            Final status dictionary

        Raises:
            TimeoutError: If operation doesn't complete within timeout
        """
        start_time = time.time()

        while True:
            status = self.check_deploy_status(async_id)

            if status["done"]:
                return status

            # Check timeout
            if time.time() - start_time > timeout:
                raise TimeoutError(
                    f"Deploy operation {async_id} did not complete within {timeout}s"
                )

            # Wait before next poll
            time.sleep(poll_interval)
