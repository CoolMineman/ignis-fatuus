#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl

/******************************************************
  ignis-fatuus:shaders/material/ultra_warm_glow.frag
******************************************************/

void frx_startFragment(inout frx_FragmentData fragData) {
	fragData.emissivity = 1.0;
	fragData.diffuse = false;
	fragData.ao = false;
}