@echo off
set JMETER_HOME=C:\apache-jmeter-5.6.2
set TEST_PLAN=performance-tests\jmeter-test-plan.jmx
set RESULTS_DIR=performance-tests\results
set LOG_FILE=%RESULTS_DIR%\load-test.log

echo Starting JMeter load test...
%JMETER_HOME%\bin\jmeter -n -t %TEST_PLAN% -l %LOG_FILE% -e -o %RESULTS_DIR%\html-report

echo Load test completed! Results saved to %RESULTS_DIR%