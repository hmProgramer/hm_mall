package com.hm.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie 工具类
 */
@Slf4j
public final class CookieUtils {

	/**
	 * 得到Cookie的值, 不编码
	 *
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		return getCookieValue(request, cookieName, null);
	}


	/**
	 * 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
		setCookie(request, response, cookieName, cookieValue, -1);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效,但不编码
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage) {
		setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
	}

	/**
	 * 设置Cookie的值 不设置生效时间,但编码
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, boolean isEncode) {
		setCookie(request, response, cookieName, cookieValue, -1, isEncode);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效, 编码参数
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
	}

	/**
	 * 删除Cookie带cookie域名
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		doSetCookie(request, response, cookieName, "", -1, false);
	}


	/**
	 * 设置Cookie的值，并使其在指定时间内生效
	 *
	 * @param cookieMaxage
	 *            cookie生效的最大秒数
	 */
	private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			} else if (isEncode) {
				cookieValue = URLEncoder.encode(cookieValue, "utf-8");
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxage > 0)
				cookie.setMaxAge(cookieMaxage);
			if (null != request)// 设置域名的cookie
				cookie.setDomain(getDomainName(request));
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			log.error("Cookie Encode Error.", e);
		}
	}

	/**
	 * 设置Cookie的值，并使其在指定时间内生效
	 *
	 * @param cookieMaxage
	 *            cookie生效的最大秒数
	 */
	private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			} else {
				cookieValue = URLEncoder.encode(cookieValue, encodeString);
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxage > 0)
				cookie.setMaxAge(cookieMaxage);
			if (null != request)// 设置域名的cookie
				cookie.setDomain(getDomainName(request));
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			log.error("Cookie Encode Error.", e);
		}
	}

	/**
	 * 得到cookie的域名
	 */
	private static final String getDomainName(HttpServletRequest request) {
		String domainName = null;

		String serverName = request.getRequestURL().toString();
		if (serverName == null || serverName.equals("")) {
			domainName = "";
		} else {
			serverName = serverName.toLowerCase();
			serverName = serverName.substring(7);
			final int end = serverName.indexOf("/");
			serverName = serverName.substring(0, end);
			final String[] domains = serverName.split("\\.");
			int len = domains.length;
			if (len > 3) {
				// www.xxx.com.cn
				domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
			} else if (len <= 3 && len > 1) {
				// xxx.com or xxx.cn
				domainName = domains[len - 2] + "." + domains[len - 1];
			} else {
				domainName = serverName;
			}
		}

		if (domainName != null && domainName.indexOf(":") > 0) {
			String[] ary = domainName.split("\\:");
			domainName = ary[0];
		}
		return domainName;
	}



	/**
	 * 得到Cookie的值,
	 *
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, String charset) {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null) {
			return null;
		}
		String retValue = null;
		try {
			for (int i = 0; i < cookieList.length; i++) {
				if (cookieList[i].getName().equals(cookieName)) {
					if (charset != null && charset.length() > 0) {
						retValue = URLDecoder.decode(cookieList[i].getValue(), charset);
					} else {
						retValue = cookieList[i].getValue();
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Cookie Decode Error.", e);
		}
		return retValue;
	}

	public static CookieBuilder newBuilder(HttpServletResponse response) {
		return new CookieBuilder(response);
	}

	public static class CookieBuilder {
		private HttpServletRequest request;
		private HttpServletResponse response;
		private Integer maxAge;
		private String charset;
		private boolean httpOnly = false;

		public CookieBuilder(HttpServletResponse response) {
			this.response = response;
		}

		public CookieBuilder request(HttpServletRequest request) {
			this.request = request;
			return this;
		}

		public CookieBuilder maxAge(int maxAge) {
			this.maxAge = maxAge;
			return this;
		}

		public CookieBuilder charset(String charset) {
			this.charset = charset;
			return this;
		}

		public CookieBuilder httpOnly() {
			this.httpOnly = true;
			return this;
		}

		public void build(String cookieName, String cookieValue) {
			try {
				if (StringUtils.isBlank(charset)) {
					charset = "utf-8";
				}

				if (cookieValue == null) {
					cookieValue = "";
				} else if (StringUtils.isNotBlank(charset)) {
					cookieValue = URLEncoder.encode(cookieValue, charset);
				}
				Cookie cookie = new Cookie(cookieName, cookieValue);
				if (maxAge != null && maxAge > 0)
					cookie.setMaxAge(maxAge);
				if (null != request)// 设置域名的cookie
					//todo
					cookie.setDomain(getDomainName(request));
				cookie.setPath("/");

				cookie.setHttpOnly(httpOnly);
				response.addCookie(cookie);
			} catch (Exception e) {
				log.error("Cookie Encode Error.", e);
			}
		}

		/**
		 * 得到cookie的域名
		 */
		private String getDomainName(HttpServletRequest request) {
			String domainName = null;

			String serverName = request.getRequestURL().toString();
			if (serverName == null || serverName.equals("")) {
				domainName = "";
			} else {
				serverName = serverName.toLowerCase();
				serverName = serverName.substring(7);
				final int end = serverName.indexOf("/");
				serverName = serverName.substring(0, end);
				final String[] domains = serverName.split("\\.");
				int len = domains.length;
				if (len > 3) {
					// www.xxx.com.cn
					domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
				} else if (len <= 3 && len > 1) {
					// xxx.com or xxx.cn
					domainName = domains[len - 2] + "." + domains[len - 1];
				} else {
					domainName = serverName;
				}
			}

			if (domainName != null && domainName.indexOf(":") > 0) {
				String[] ary = domainName.split("\\:");
				domainName = ary[0];
			}
			return domainName;
		}



	}
}
