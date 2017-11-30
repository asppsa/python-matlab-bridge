function ret = jmessenger(varargin)
  if exist('github.arokem.pymatbridge.Messenger') == 0
    jmessenger_addpath(fullfile(fileparts(mfilename('fullpath')), 'jar'));
  end

  switch varargin{1}
    case 'init'
      jmessenger_init(varargin{2});
    case 'listen'
      ret = jmessenger_listen();
    case 'respond'
      jmessenger_respond(varargin{2});
    case 'exit'
      jmessenger_exit();
  end
end

function jmessenger_addpath(jardir)
  jars = dir(fullfile(jardir, '*.jar'));
  for i = 1:length(jars)
    javaaddpath(fullfile(jardir, jars(i).name));
  end
end

function jmessenger_init(socket)
  global jmessenger_messenger;

  fprintf('Starting server on %s ...\n', socket);
  jmessenger_messenger = github.arokem.pymatbridge.Messenger(socket);
end

function msg_in = jmessenger_listen()
  global jmessenger_messenger;

  not_done = true;
  while not_done
    try
      str = jmessenger_messenger.listen();
    catch
      ex = lasterror();
      ex
      warning(['Listen error: ', ex.message]);
    end

    drawnow();
    not_done = isempty(str);
  end

  msg_in = char(str);
end

function jmessenger_respond(resp)
  global jmessenger_messenger;

  try
    jmessenger_messenger.respond(resp);
  catch
    ex = lasterror();
    ex
    warning(['Respond error: ', ex.message]);
  end
end

function jmessenger_exit()
  global jmessenger_messenger;

  jmessenger_messenger.exit();
end