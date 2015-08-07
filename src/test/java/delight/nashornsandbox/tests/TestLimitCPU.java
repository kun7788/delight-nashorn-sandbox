package delight.nashornsandbox.tests;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.junit.Test;

@SuppressWarnings("all")
public class TestLimitCPU {
  @Test
  public void test() {
    final NashornSandbox sandbox = NashornSandboxes.create();
    sandbox.eval("java.lang.System.exit(0);");
  }
}