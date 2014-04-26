#version 120

uniform sampler2D tex0;
uniform sampler2D depthTex;

uniform float ticks;

uniform float brownshrooms;
uniform float redshrooms;
uniform vec4 harmoniumColor;

void main()
{
	gl_FragColor = texture2D(tex0, gl_TexCoord[0].st);
	float fogCoord = texture2D(depthTex, gl_TexCoord[0].st).r;

	if(redshrooms > 0.0)
	{
		float r = (sin((fogCoord - ticks) / 5.0) - 0.4) * redshrooms;
     	   
		if(r > 0.0)
			gl_FragColor.r += r;
	}
    
	if(redshrooms > 0.0)
	{
		gl_FragColor.rgb = mix(gl_FragColor.rgb, getRotatedColor(gl_FragColor.rgb, mod(ticks + gl_FogFragCoord, 50.0) / 50.0), clamp(redshrooms * 1.5, 0.0, 1.0));
	}

	if(harmoniumColor.a > 0.0)
	{
		vec3 c1 = gl_FragColor.rgb;
		vec3 c2 = harmoniumColor.rgb;
        
		float distR = sqrt((c1.r - c2.r) * (c1.r - c2.r));
		float distG = sqrt((c1.g - c2.g) * (c1.g - c2.g));
		float distB = sqrt((c1.b - c2.b) * (c1.b - c2.b));
        
		float dist = clamp((distR + distG + distB), 0.0, 1.0);
		for (int i = 0; i < 4; i++)
			dist *= dist;
		float harmonizeStrength = dist * 3.0;
        
		float disk = (sin((gl_FogFragCoord - ticks) * 0.1434234) - 0.4) * 0.8;
		harmonizeStrength += disk;

		float disk2 = (sin((gl_FogFragCoord - ticks) * -0.12313) - 0.2) * 0.8;
		harmonizeStrength += disk2;

		float disk3 = sin((gl_FogFragCoord - ticks) * -0.051233) * 0.2 * sin(ticks * 0.1321334);
		harmonizeStrength += disk3;

		harmonizeStrength = clamp(harmonizeStrength, 0.0, 3.0);
        
		vec3 harmonizedColor;
        
		if (harmonizeStrength < 1.0)
		{
			harmonizedColor = mix(harmoniumColor.rgb, vec3(0.5), harmonizeStrength); // Max of 1.0
		}
		else
		{
			harmonizedColor = mix(vec3(0.5), vec3(1.0) - harmoniumColor.rgb, (harmonizeStrength - 1.0) * 0.5); // Max of 2.0
		}
        
		gl_FragColor.rgb = mix(gl_FragColor.rgb, harmonizedColor, harmoniumColor.a);
	}
}