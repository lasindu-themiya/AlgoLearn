# Sorting and Searching Algorithms API

This document provides examples and usage instructions for the sorting and searching algorithms implemented in the AlgoPulse API.

## Authentication

All endpoints require JWT authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Sorting Algorithms

### Available Sorting Endpoints

1. `POST /api/sort/bubble` - Bubble Sort
2. `POST /api/sort/insertion` - Insertion Sort  
3. `POST /api/sort/selection` - Selection Sort
4. `POST /api/sort/min` - Min Sort
5. `POST /api/sort/optimized-bubble` - Optimized Bubble Sort

### Request Format

```json
{
  "sessionId": "optional-session-id",
  "array": [64, 34, 25, 12, 22, 11, 90]
}
```

### Example: Bubble Sort

**Request:**
```bash
curl -X POST "http://localhost:8080/api/sort/bubble" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "array": [64, 34, 25, 12, 22, 11, 90]
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Bubble sort completed successfully",
  "session": {
    "id": "...",
    "sessionId": "generated-uuid",
    "userId": "user123",
    "algorithm": "bubble",
    "originalArray": [64, 34, 25, 12, 22, 11, 90],
    "currentArray": [11, 12, 22, 25, 34, 64, 90],
    "completed": true,
    "comparisons": 21,
    "swaps": 13,
    "steps": [...],
    "operationHistory": [...],
    "createdAt": 1727343134000,
    "updatedAt": 1727343134500
  },
  "sortedArray": [11, 12, 22, 25, 34, 64, 90],
  "comparisons": 21,
  "swaps": 13,
  "steps": [
    {
      "array": [64, 34, 25, 12, 22, 11, 90],
      "compareIndex1": -1,
      "compareIndex2": -1,
      "swapped": false,
      "description": "Initial array",
      "timestamp": 1727343134100
    },
    {
      "array": [64, 34, 25, 12, 22, 11, 90],
      "compareIndex1": 0,
      "compareIndex2": 1,
      "swapped": false,
      "description": "Comparing arr[0]=64 with arr[1]=34",
      "timestamp": 1727343134101
    },
    {
      "array": [34, 64, 25, 12, 22, 11, 90],
      "compareIndex1": 0,
      "compareIndex2": 1,
      "swapped": true,
      "description": "Swapped arr[0] and arr[1]",
      "timestamp": 1727343134102
    }
    // ... more steps
  ]
}
```

### Step-by-Step Visualization

Each sorting algorithm returns detailed steps for visualization:

- `array`: Snapshot of the array at this step
- `compareIndex1`, `compareIndex2`: Indices being compared
- `swapped`: Whether a swap occurred
- `description`: Human-readable description of the step
- `timestamp`: When this step occurred

## Searching Algorithms

### Available Search Endpoints

1. `POST /api/search/linear` - Linear Search
2. `POST /api/search/binary` - Binary Search (requires sorted array)

### Request Format

```json
{
  "sessionId": "optional-session-id",
  "array": [11, 12, 22, 25, 34, 64, 90],
  "target": 25
}
```

### Example: Linear Search

**Request:**
```bash
curl -X POST "http://localhost:8080/api/search/linear" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "array": [64, 34, 25, 12, 22, 11, 90],
    "target": 25
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Target 25 found at index 2",
  "session": {
    "id": "...",
    "sessionId": "generated-uuid",
    "userId": "user123",
    "algorithm": "linear",
    "array": [64, 34, 25, 12, 22, 11, 90],
    "target": 25,
    "found": true,
    "foundIndex": 2,
    "comparisons": 3,
    "steps": [...],
    "operationHistory": [...],
    "createdAt": 1727343134000,
    "updatedAt": 1727343134300
  },
  "found": true,
  "foundIndex": 2,
  "comparisons": 3,
  "steps": [
    {
      "currentIndex": -1,
      "value": -1,
      "match": false,
      "left": -1,
      "right": -1,
      "mid": -1,
      "description": "Starting linear search for target 25 in array [64, 34, 25, 12, 22, 11, 90]",
      "timestamp": 1727343134100
    },
    {
      "currentIndex": 0,
      "value": 64,
      "match": false,
      "left": -1,
      "right": -1,
      "mid": -1,
      "description": "Checking arr[0] = 64 != target 25",
      "timestamp": 1727343134101
    },
    {
      "currentIndex": 1,
      "value": 34,
      "match": false,
      "left": -1,
      "right": -1,
      "mid": -1,
      "description": "Checking arr[1] = 34 != target 25",
      "timestamp": 1727343134102
    },
    {
      "currentIndex": 2,
      "value": 25,
      "match": true,
      "left": -1,
      "right": -1,
      "mid": -1,
      "description": "Checking arr[2] = 25 == (MATCH!) target 25",
      "timestamp": 1727343134103
    },
    {
      "currentIndex": 2,
      "value": 25,
      "match": true,
      "left": -1,
      "right": -1,
      "mid": -1,
      "description": "Target 25 found at index 2!",
      "timestamp": 1727343134104
    }
  ]
}
```

### Example: Binary Search

**Request:**
```bash
curl -X POST "http://localhost:8080/api/search/binary" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "array": [11, 12, 22, 25, 34, 64, 90],
    "target": 25
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Target 25 found at index 3",
  "session": {
    "id": "...",
    "sessionId": "generated-uuid",
    "userId": "user123",
    "algorithm": "binary",
    "array": [11, 12, 22, 25, 34, 64, 90],
    "target": 25,
    "found": true,
    "foundIndex": 3,
    "comparisons": 2,
    "steps": [...],
    "operationHistory": [...],
    "createdAt": 1727343134000,
    "updatedAt": 1727343134200
  },
  "found": true,
  "foundIndex": 3,
  "comparisons": 2,
  "steps": [
    {
      "currentIndex": -1,
      "value": -1,
      "match": false,
      "left": -1,
      "right": -1,
      "mid": -1,
      "description": "Starting binary search for target 25 in sorted array [11, 12, 22, 25, 34, 64, 90]",
      "timestamp": 1727343134100
    },
    {
      "currentIndex": 3,
      "value": 25,
      "match": false,
      "left": 0,
      "right": 6,
      "mid": 3,
      "description": "Searching in range [0, 6]. Mid = 3, arr[3] = 25",
      "timestamp": 1727343134101
    },
    {
      "currentIndex": 3,
      "value": 25,
      "match": true,
      "left": 0,
      "right": 6,
      "mid": 3,
      "description": "Target 25 found at index 3!",
      "timestamp": 1727343134102
    }
  ]
}
```

## Session Management

### Get Session Details
```bash
GET /api/sort/session/{sessionId}
GET /api/search/session/{sessionId}
```

### Get All User Sessions
```bash
GET /api/sort/sessions
GET /api/search/sessions
```

### Delete Session
```bash
DELETE /api/sort/session/{sessionId}
DELETE /api/search/session/{sessionId}
```

## Algorithm Complexities

### Sorting Algorithms

| Algorithm | Best Case | Average Case | Worst Case | Space |
|-----------|-----------|--------------|------------|-------|
| Bubble Sort | O(n) | O(n²) | O(n²) | O(1) |
| Optimized Bubble | O(n) | O(n²) | O(n²) | O(1) |
| Insertion Sort | O(n) | O(n²) | O(n²) | O(1) |
| Selection Sort | O(n²) | O(n²) | O(n²) | O(1) |
| Min Sort | O(n²) | O(n²) | O(n²) | O(1) |

### Searching Algorithms

| Algorithm | Best Case | Average Case | Worst Case | Space | Requirements |
|-----------|-----------|--------------|------------|-------|--------------|
| Linear Search | O(1) | O(n) | O(n) | O(1) | None |
| Binary Search | O(1) | O(log n) | O(log n) | O(1) | Sorted Array |

## Error Handling

### Common Error Responses

```json
{
  "success": false,
  "message": "Array cannot be null or empty"
}
```

```json
{
  "success": false,
  "message": "Array size cannot exceed 100 elements"
}
```

```json
{
  "success": false,
  "message": "Binary search requires a sorted array. Please sort the array first."
}
```

```json
{
  "success": false,
  "message": "Session not found"
}
```

## JWT Security

All endpoints are secured using JWT authentication. The JWT token:

- Must be included in the Authorization header as a Bearer token
- Is validated on each request
- Contains the userId which is used to scope all data operations
- Ensures users can only access their own sessions and data

The JWT authentication is handled by the existing `JwtAuthenticationFilter` which:
- Extracts the userId from the JWT token
- Validates the token
- Adds the userId to request attributes for use in controllers
- Ensures data isolation between users

## Usage Examples

### Complete Workflow Example

```javascript
// 1. Sort an array using bubble sort
const sortResponse = await fetch('/api/sort/bubble', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer ' + jwtToken,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    array: [64, 34, 25, 12, 22, 11, 90]
  })
});

const sortResult = await sortResponse.json();
const sortedArray = sortResult.sortedArray;
const sessionId = sortResult.session.sessionId;

// 2. Use the sorted array for binary search
const searchResponse = await fetch('/api/search/binary', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer ' + jwtToken,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    array: sortedArray,
    target: 25
  })
});

const searchResult = await searchResponse.json();
console.log('Found:', searchResult.found);
console.log('Index:', searchResult.foundIndex);

// 3. Get session details for visualization
const sessionResponse = await fetch(`/api/sort/session/${sessionId}`, {
  headers: {
    'Authorization': 'Bearer ' + jwtToken
  }
});

const sessionData = await sessionResponse.json();
// Use sessionData.session.steps for step-by-step visualization
```

This API provides comprehensive sorting and searching functionality with detailed step-by-step information for educational visualization purposes.