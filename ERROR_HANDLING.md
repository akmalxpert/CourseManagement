# Error Handling & Logging Guide

This document describes the enhanced error handling and logging implementation in the Course Management System.

## 🚨 Error Handling Overview

The application implements a comprehensive error handling strategy with:

- **Global Exception Handler**: Centralized error management
- **Structured Logging**: Proper logging with error IDs for tracking
- **No Stack Trace Exposure**: Security-focused error responses
- **Standardized Error Format**: Consistent API error responses

## 📝 Logging Architecture

### Log Levels
- **ERROR**: Critical errors requiring immediate attention
- **WARN**: Warning conditions and handled exceptions
- **INFO**: General application flow and business operations
- **DEBUG**: Detailed diagnostic information

### Log Files
- **`logs/app.log`**: General application logs (rotated daily, 10MB max)
- **`logs/error.log`**: Error and warning logs only (rotated daily, 5MB max)
- **Console Output**: Real-time log viewing during development

### Log Configuration
Logs are configured via `log4j2.yml` with:
- Environment-based log levels (`LOG_LEVEL` environment variable)
- Automatic file rotation and compression
- Separate error file for better monitoring
- Thread-safe logging with performance optimization

## 🛡️ Security Features

### Error ID Generation
Each error generates a unique 8-character error ID for tracking:
```
[WARN] 2023-12-07 10:15:30.123 [http-nio-8080-exec-1] GlobalExceptionHandler - 
Validation error [A1B2C3D4]: Course Name is missing or empty - Request: POST /api/course
```

### No Stack Trace Exposure
- Stack traces are never exposed to API clients
- Internal errors show user-friendly messages
- Stack traces are logged internally for debugging
- Error IDs allow correlation between user reports and logs

### Request Information Logging
Error logs include:
- HTTP method and endpoint
- Error ID for tracking
- Timestamp and thread information
- User-friendly error messages

## 📊 Error Categories

### 1. Validation Errors (400 - Bad Request)
**Triggers:**
- Missing mandatory fields
- Invalid data format
- Business rule violations

**Example Response:**
```json
{
  "data": null,
  "detail": "Course Name is missing or empty",
  "statusCode": 400
}
```

**Logging:**
```
[WARN] GlobalExceptionHandler - Validation error [A1B2C3D4]: Course Name is missing or empty
```

### 2. Not Found Errors (404 - Not Found)
**Triggers:**
- Resource doesn't exist
- Invalid ID provided

**Example Response:**
```json
{
  "data": null,
  "detail": "Course not found",
  "statusCode": 404
}
```

### 3. Data Integrity Errors (400 - Bad Request)
**Triggers:**
- Foreign key constraints
- Database integrity violations

**Example Response:**
```json
{
  "data": null,
  "detail": "This operation affects some other entities. Please delete all related entities to avoid this error.",
  "statusCode": 400
}
```

### 4. Unexpected Errors (500 - Internal Server Error)
**Triggers:**
- Unhandled exceptions
- System-level errors

**Example Response:**
```json
{
  "data": null,
  "detail": "An unexpected error occurred. Please contact support with error ID: A1B2C3D4",
  "statusCode": 500
}
```

**Logging:**
```
[ERROR] GlobalExceptionHandler - Unexpected error [A1B2C3D4]: NullPointerException: ... - Stack trace: 
java.lang.NullPointerException: ...
  at com.example.service...
```

## 🔧 Configuration

### Environment Variables
Control logging behavior with environment variables:

```env
# Logging Configuration
LOG_LEVEL=INFO  # Options: ERROR, WARN, INFO, DEBUG
```

### Log Level Guidelines
- **Production**: `INFO` or `WARN`
- **Staging**: `INFO` or `DEBUG`
- **Development**: `DEBUG`

## 📈 Monitoring & Alerting

### Error ID Tracking
1. **User Reports**: Users can provide error IDs for support
2. **Log Correlation**: Search logs by error ID for full context
3. **Monitoring**: Alert on ERROR level logs
4. **Analytics**: Track error patterns and frequencies

### Log Analysis
Use log analysis tools to:
- Monitor error rates and patterns
- Set up alerts for critical errors
- Track performance and usage patterns
- Identify and resolve issues proactively

## 🔍 Troubleshooting

### Common Issues

1. **High Error Rates**
   - Check application logs for patterns
   - Verify database connectivity
   - Review recent deployments

2. **Performance Issues**
   - Enable DEBUG logging temporarily
   - Monitor database query performance
   - Check for resource constraints

3. **Validation Errors**
   - Review input validation rules
   - Check API documentation
   - Verify client request format

### Debug Mode
Enable debug logging for detailed diagnostics:
```env
LOG_LEVEL=DEBUG
```

**Note**: Debug mode generates verbose logs - use sparingly in production.

## 📋 Best Practices

### For Developers
1. **Use appropriate log levels**: INFO for business logic, DEBUG for technical details
2. **Include context**: Log relevant IDs and parameters
3. **Avoid sensitive data**: Never log passwords, tokens, or personal information
4. **Use error IDs**: Include error IDs in user-facing error messages

### For Operations
1. **Monitor error logs**: Set up alerts for ERROR and WARN levels
2. **Log rotation**: Ensure log files are rotated and archived properly
3. **Performance**: Monitor log file sizes and system impact
4. **Security**: Protect log files from unauthorized access

## 🚀 Production Considerations

### Log Management
- **Centralized Logging**: Consider ELK stack or similar for production
- **Log Shipping**: Forward logs to monitoring systems
- **Retention Policy**: Define log retention and archival policies
- **Security**: Encrypt logs in transit and at rest

### Monitoring Integration
- **Health Checks**: Include logging health in application monitoring
- **Alerting Rules**: Set up alerts for error rate thresholds
- **Dashboard**: Create dashboards for error trends and patterns
- **Incident Response**: Use error IDs for faster incident resolution

## 📞 Support

When reporting issues, include:
1. **Error ID** from the error message
2. **Timestamp** when the error occurred
3. **Steps to reproduce** the issue
4. **Expected behavior** vs actual behavior

This information helps support teams quickly locate and resolve issues using the comprehensive logging system.
