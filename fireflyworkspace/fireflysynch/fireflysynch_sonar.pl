%====================================================================================
% fireflysynch_sonar description   
%====================================================================================
dispatch( cellstate, cellstate(X,Y,COLOR) ). %commute cell state on grid
event( sonardata, distance(D) ). %emitted by sonar
event( sync, sync(TIMER) ). %shared frequency from orchestrator
event( unsync, unsync(CMD) ). %cancel sync and return to random
%====================================================================================
context(ctxfireflysynch, "localhost",  "TCP", "8040").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( griddisplay, ctxgrid, "external").
  qactor( sonarmock, ctxfireflysynch, "it.unibo.sonarmock.Sonarmock").
 static(sonarmock).
  qactor( orchestrator, ctxfireflysynch, "it.unibo.orchestrator.Orchestrator").
 static(orchestrator).
  qactor( firefly, ctxfireflysynch, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
