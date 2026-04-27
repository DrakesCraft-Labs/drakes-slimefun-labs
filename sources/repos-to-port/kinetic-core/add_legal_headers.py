"""
Script to add legal headers to all Python files in kinetic-core
"""

import os
from pathlib import Path

# Legal header to add
LEGAL_HEADER = '''"""
Kinetic Core - Salesforce Integration Library
Copyright (c) 2025 Antonio Trento (https://antoniotrento.net)

This file is part of Kinetic Core, the foundational library powering KineticMCP.

Project: https://github.com/antonio-backend-projects/kinetic-core
Website: https://kineticmcp.com
Author: Antonio Trento
License: MIT (see LICENSE file for details)

Part of the KineticMCP ecosystem - AI-powered Salesforce integration tools.
"""
'''

def has_legal_header(content: str) -> bool:
    """Check if file already has legal header"""
    return 'Copyright (c) 2025 Antonio Trento' in content or \
           'kineticmcp.com' in content

def add_header_to_file(file_path: Path):
    """Add legal header to a Python file"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Skip if already has header
    if has_legal_header(content):
        print(f"  [SKIP] Already has header: {file_path}")
        return False

    # Handle files that start with docstring
    if content.strip().startswith('"""') or content.strip().startswith("'''"):
        # Find end of first docstring
        delimiter = '"""' if content.strip().startswith('"""') else "'''"
        first_delimiter_pos = content.find(delimiter)
        second_delimiter_pos = content.find(delimiter, first_delimiter_pos + 3)

        if second_delimiter_pos != -1:
            # Insert header before the existing docstring
            old_docstring = content[first_delimiter_pos:second_delimiter_pos + 3]
            rest = content[second_delimiter_pos + 3:]
            new_content = LEGAL_HEADER + '\n' + old_docstring + rest
        else:
            # Malformed docstring, add at top
            new_content = LEGAL_HEADER + '\n' + content
    else:
        # No docstring, add at top
        new_content = LEGAL_HEADER + '\n' + content

    # Write back
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)

    print(f"  [ADD] Added header: {file_path}")
    return True

def main():
    """Add legal headers to all Python files"""
    print("Adding legal headers to kinetic-core files...\n")

    # Get all Python files in kinetic_core
    python_files = list(Path('kinetic_core').rglob('*.py'))

    added_count = 0
    skipped_count = 0

    for file_path in sorted(python_files):
        if add_header_to_file(file_path):
            added_count += 1
        else:
            skipped_count += 1

    print(f"\n{'='*60}")
    print(f"Summary:")
    print(f"  Added headers: {added_count}")
    print(f"  Already present: {skipped_count}")
    print(f"  Total files: {len(python_files)}")
    print(f"{'='*60}")

if __name__ == '__main__':
    main()
