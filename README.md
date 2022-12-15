Walks one or more directories capturing the file/directory tree into a JSON format.

## Usage

```
Usage: file-tree-as-json [-h] [--debug] [-o=FILE] PATH...
      PATH...         One or more paths to process
      --debug         Enable debug logs
  -h, --help          Show usage
  -o, --output=FILE   If not provided, JSON is output to stdout
```

## Example output

```json
{
  "C:\\projects\\file-tree-as-json\\src" : {
    "children" : [ {
      "path" : "main",
      "children" : [ {
        "path" : "java",
        "children" : [ {
          "path" : "app",
          "children" : [ {
            "path" : "FilenameSerializer.java"
          }, {
            "path" : "FileTreeAccumulator.java"
          }, {
            "path" : "FileTreeAsJson.java"
          }, {
            "path" : "FileTreeEntry.java"
          } ]
        } ]
      }, {
        "path" : "resources",
        "children" : [ {
          "path" : "logback.xml"
        }, {
          "path" : "schema.json"
        } ]
      } ]
    }, {
      "path" : "test",
      "children" : [ {
        "path" : "java",
        "children" : [ ]
      }, {
        "path" : "resources",
        "children" : [ ]
      } ]
    } ]
  }
}
```

## Output Schema

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "description": "Map of root paths to its tree",
  "type": "object",
  "additionalProperties": {
    "type": "object",
    "properties": {
      "children": {
        "type": "array",
        "items": {
          "$ref": "#/definitions/FileTreeEntry"
        }
      }
    }
  },
  "definitions": {
    "FileTreeEntry": {
      "type": "object",
      "properties": {
        "path": {
          "description": "The subpath of this entry",
          "type": "string"
        },
        "children": {
          "description": "If present, indicates that this subpath is a directory with the given entries",
          "type": "array",
          "items": {
            "$ref": "#/definitions/FileTreeEntry"
          }
        }
      },
      "required": ["path"]
    }
  }
}
```