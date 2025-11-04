import React, { useState } from "react";
import {
  ChevronLeftIcon,
  ChevronRightIcon,
  PlusIcon,
  EllipsisVerticalIcon,
  MagnifyingGlassIcon,
  ClockIcon,
} from "@heroicons/react/24/outline";
import {
  CheckCircleIcon,
  ChartBarIcon,
} from "@heroicons/react/24/solid";

const teams = [
  {
    name: "Marketing team",
    time: "Meeting at 9:00 - 11:30am",
    avatars: ["M", "A", "J"],
    bg: "bg-[#F5FBEF]",
  },
  {
    name: "Sales team",
    time: "Meeting at 4:00 - 5:00 pm",
    avatars: ["S", "L", "P"],
    bg: "bg-[#E6F7FF]",
  },
];

export default function Dashboard() {
  const [teamIdx, setTeamIdx] = useState(0);

  const next = () => setTeamIdx((i) => (i + 2 >= teams.length ? 0 : i + 2));
  const prev = () => setTeamIdx((i) => (i - 2 < 0 ? Math.max(teams.length - 2, 0) : i - 2));

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      {/* SEARCH BAR - EXACT */}
     <div className="mb-8">
       <div className="relative max-w-md">
         <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
         <input
           type="text"
           placeholder="Search tasks"
           className="w-full pl-10 pr-4 py-2.5 text-sm border-b border-gray-300 focus:outline-none focus:border-purple-600 bg-transparent"
         />
       </div>
     </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* LEFT COLUMN */}
       <section className="lg:col-span-2 space-y-8">
         {/* TODAY TASKS - EXACT DESIGN */}
         <div className="bg-white-100 rounded-2xl shadow-sm p-6">
           <div className="flex items-center justify-between mb-4">
             <h2 className="text-xl font-semibold text-[#1D0E3D]">Today tasks</h2>
             <div className="flex items-center space-x-1">
               <button className="p-1.5 hover:bg-gray-100 rounded-lg">
                 <PlusIcon className="w-5 h-5 text-gray-500" />
               </button>
               <button className="p-1.5 hover:bg-gray-100 rounded-lg">
                 <ChevronLeftIcon className="w-5 h-5 text-gray-500" />
               </button>
               <button className="p-1.5 hover:bg-gray-100 rounded-lg">
                 <ChevronRightIcon className="w-5 h-5 text-gray-500" />
               </button>
             </div>
           </div>

           <div className="flex space-x-6 overflow-hidden">
             {teams.slice(teamIdx, teamIdx + 2).map((team, i) => (
               <div
                 key={i}
                 className={`${team.bg} rounded-2xl p-5 min-w-[300px] flex flex-col justify-between shadow-sm`}
               >
                 {/* Top Row */}
                 <div className="flex justify-between items-start">
                   <div>
                     <p className="text-lg font-semibold text-gray-800">{team.name}</p>
                     <div className="flex items-center space-x-1 mt-1 text-sm text-gray-600">
                       <ClockIcon className="w-4 h-4" />
                       <span>{team.time}</span>
                     </div>
                   </div>
                   <button className="p-1 hover:bg-white/30 rounded">
                     <EllipsisVerticalIcon className="w-5 h-5 text-gray-600" />
                   </button>
                 </div>

                 {/* Bottom Row */}
                 <div className="flex justify-between items-end mt-6">
                   <div className="flex -space-x-2">
                     {team.avatars.map((initial, idx) => (
                       <img
                         key={idx}
                         src={`https://ui-avatars.com/api/?background=6B46C1&color=fff&name=${initial}&bold=true&size=36`}
                         alt={initial}
                         className="w-9 h-9 rounded-full border-2 border-white"
                       />
                     ))}
                   </div>

                   {/* Icon Group with White Background */}
                   <div className="flex space-x-1 bg-white-150 rounded-lg p-1.5 shadow-sm">
                     {/* Video Call Icon */}
                     <button className="p-1.5 text-[#1D0E3D] rounded-md hover:bg-[#1D0E3D] hover:text-white transition-colors duration-200">
                       <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                         <path d="M17 10.5V7c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1v10c0 .55.45 1 1 1h12c.55 0 1-.45 1-1v-3.5l4 4v-11l-4 4z"/>
                       </svg>
                     </button>

                     {/* Voice Call Icon */}
                     <button className="p-1.5 text-[#1D0E3D] rounded-md hover:bg-[#1D0E3D] hover:text-white transition-colors duration-200">
                       <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                         <path d="M20 15.5c-1.25 0-2.45-.2-3.57-.57-.35-.11-.74-.03-1.02.24l-2.2 2.2c-2.83-1.44-5.15-3.75-6.59-6.59l2.2-2.21c.28-.26.36-.65.25-1C8.7 6.45 8.5 5.25 8.5 4c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1 0 9.39 7.61 17 17 17 .55 0 1-.45 1-1v-3.5c0-.55-.45-1-1-1z"/>
                       </svg>
                     </button>

                     {/* Message Icon */}
                     <button className="p-1.5 text-[#1D0E3D] rounded-md hover:bg-[#1D0E3D] hover:text-white transition-colors duration-200">
                       <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                         <path d="M20 2H4c-1.1 0-1.99.9-1.99 2L2 22l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 12H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
                       </svg>
                     </button>
                   </div>
                 </div>
               </div>
             ))}
           </div>
         </div>

          {/* DAILY ACTIVITY - WITH SIDE BY SIDE CARDS */}
          <div className="bg-white-100 rounded-2xl shadow-sm p-6">
            <div className="flex items-center justify-between mb-5">
              <h2 className="text-xl font-semibold text-[#1D0E3D]">Daily activity</h2>
              <button className="text-sm text-gray-500 flex items-center space-x-1">
                <span>Filter</span>
                <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                </svg>
              </button>
            </div>

            <div className="space-y-6">
              {/* LINE CHART */}
              <div className="relative h-48">
                <svg className="w-full h-full" viewBox="0 0 400 160" preserveAspectRatio="none">
                  <defs>
                    <linearGradient id="areaGrad" x1="0%" y1="0%" x2="0%" y2="100%">
                      <stop offset="0%" stopColor="#A0F1EA" />
                      <stop offset="100%" stopColor="#E6F7FF" />
                    </linearGradient>
                  </defs>
                  <path
                    d="M0,140 Q50,120 100,100 Q150,80 200,90 Q250,70 300,60 Q350,55 400,50 L400,160 L0,160 Z"
                    fill="url(#areaGrad)"
                  />
                  <path
                    d="M0,140 Q50,120 100,100 Q150,80 200,90 Q250,70 300,60 Q350,55 400,50"
                    fill="none"
                    stroke="#5CE1E6"
                    strokeWidth="3"
                    strokeLinecap="round"
                  />
                </svg>

                {/* Y-axis */}
                <div className="absolute left-0 top-0 h-full flex flex-col justify-between text-xs text-gray-500 pl-2">
                  <span>75%</span>
                  <span>50%</span>
                  <span>25%</span>
                  <span>0%</span>
                </div>

                {/* X-axis */}
                <div className="absolute bottom-0 left-0 w-full flex justify-between text-xs text-gray-500 px-2">
                  {["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug"].map((m) => (
                    <span key={m} className="text-[10px]">{m}</span>
                  ))}
                </div>
              </div>

              {/* STAT CARDS - SIDE BY SIDE */}
              <div className="grid grid-cols-2 gap-4">
                {/* Total Completed */}
                <div className="bg-[#1A0B2E] text-white rounded-xl p-4 relative overflow-hidden">
                  <div className="flex items-center justify-between mb-2">
                    <p className="text-sm opacity-80">Total completed</p>
                    <CheckCircleIcon className="w-5 h-5 text-cyan-400" />
                  </div>
                  <p className="text-2xl font-bold">26 %</p>
                  <p className="text-xs opacity-70">5 of 10</p>
                  <svg className="absolute bottom-0 right-0 w-20 h-16 opacity-30" viewBox="0 0 80 60">
                    <path d="M0,50 Q20,40 40,45 Q60,35 80,30" fill="none" stroke="#5CE1E6" strokeWidth="3" strokeLinecap="round" />
                  </svg>
                </div>

                {/* Approved */}
                <div className="bg-[#6B46C1] text-white rounded-xl p-4 relative overflow-hidden">
                  <div className="flex items-center justify-between mb-2">
                    <p className="text-sm opacity-80">Approved</p>
                    <ChartBarIcon className="w-5 h-5 text-purple-300" />
                  </div>
                  <p className="text-2xl font-bold">44 %</p>
                  <p className="text-xs opacity-70">4 of 10</p>
                  <svg className="absolute bottom-0 right-0 w-20 h-16 opacity-30" viewBox="0 0 80 60">
                    <path d="M0,45 Q20,35 40,40 Q60,30 80,25" fill="none" stroke="#A78BFA" strokeWidth="3" strokeLinecap="round" />
                  </svg>
                </div>
              </div>

              {/* Planning Work */}
              <div className="mt-6 flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="w-8 h-8 bg-green-500 rounded-full flex items-center justify-center">
                    <span className="text-white text-xs font-bold">P</span>
                  </div>
                  <div>
                    <p className="font-medium text-gray-800">Planning work</p>
                    <p className="text-xs text-gray-500">Sales - Edited 15 min ago</p>
                  </div>
                </div>
                <ChevronRightIcon className="w-5 h-5 text-gray-400" />
              </div>
            </div>
          </div>
        </section>

        {/* RIGHT COLUMN */}
        <section className="space-y-8">
          {/* Calendar */}
          <div className="bg-white-100 rounded-2xl shadow-sm p-6">
            <div className="flex items-center justify-between mb-3">
              <h2 className="text-xl font-semibold text-[#1D0E3D]">May</h2>
              <div className="flex space-x-1">
                <button className="p-1 hover:bg-gray-100 rounded">
                  <ChevronLeftIcon className="w-4 h-4 text-gray-500" />
                </button>
                <button className="p-1 hover:bg-gray-100 rounded">
                  <ChevronRightIcon className="w-4 h-4 text-gray-500" />
                </button>
              </div>
            </div>

            <div className="grid grid-cols-7 gap-1 text-center text-sm">
              {["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"].map((d) => (
                <div key={d} className="font-medium text-gray-600 py-1">
                  {d}
                </div>
              ))}

              {Array(4).fill(null).map((_, i) => (
                <div key={`empty-${i}`} />
              ))}

              {Array.from({ length: 31 }, (_, i) => i + 1).map((day) => {
                const isToday = day === 11;
                return (
                  <div
                    key={day}
                    className={`py-1.5 rounded-lg text-sm ${
                      isToday
                        ? "bg-[#6B46C1] text-white font-semibold"
                        : "text-gray-800"
                    }`}
                  >
                    {day}
                  </div>
                );
              })}
            </div>
          </div>

          {/* Team Chat */}
          <div className="bg-white-100 rounded-2xl shadow-sm p-6 flex flex-col h-120">
            <div className="flex items-center justify-between mb-3">
              <h2 className="text-xl font-semibold text-[#1D0E3D]">Team chat</h2>
              <button className="relative">
                <div className="flex -space-x-2">
                  {["J", "A", "M"].map((l, i) => (
                    <img
                      key={i}
                      src={`https://ui-avatars.com/api/?background=6B46C1&color=fff&name=${l}&bold=true&size=28`}
                      alt={l}
                      className="w-7 h-7 rounded-full border-2 border-white"
                    />
                  ))}
                </div>
                <span className="absolute -top-1 -right-1 bg-green-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center">
                  +
                </span>
              </button>
            </div>

            <div className="flex-1 overflow-y-auto space-y-3 pr-2">
              <ChatBubble
                align="right"
                color="bg-green-100"
                text="Hey! I remind you of our meeting today at 4:00. See you on a video call"
                time="12 min ago"
                avatar="https://ui-avatars.com/api/?background=6B46C1&color=fff&name=H&bold=true&size=32"
              />
              <ChatBubble
                align="left"
                color="bg-purple-100"
                text="OK! I will take analytical data."
                time="14 min ago"
                avatar="https://ui-avatars.com/api/?background=6B46C1&color=fff&name=O&bold=true&size=32"
              />
              <ChatBubble
                align="right"
                color="bg-green-100"
                text="Hey! I will not be able to attend"
                time="14 min ago"
                avatar="https://ui-avatars.com/api/?background=6B46C1&color=fff&name=H&bold=true&size=32"
              />
            </div>

            <div className="mt-4">
              <div className="flex gap-0 items-center rounded-xl pl-4 pr-2 py-1 bg-gray-100 focus-within:ring-0">
                <input
                  type="text"
                  placeholder="Type message"
                  className="flex-1 text-sm py-2.5 focus:outline-none bg-transparent"
                />
                <div className="flex items-center space-x-1">
                  {/* Send Icon */}
                  <button className="p-2 text-[#1D0E3D] hover:bg-gray-200 rounded-md">
                    <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
                    </svg>
                  </button>
                </div>
              </div>

            </div>
          </div>
        </section>
      </div>
    </div>
  );
}

// Reusable Chat Bubble
function ChatBubble({ align, color, text, time, avatar }) {
  return (
    <div
      className={`flex space-x-2 ${align === "right" ? "flex-row-reverse space-x-reverse" : ""}`}
    >
      <img src={avatar} alt="" className="w-8 h-8 rounded-full flex-shrink-0" />
      <div className={`flex flex-col ${align === "right" ? "items-end" : ""}`}>
        <div
          className={`max-w-xs ${color} rounded-2xl px-4 py-2 text-sm text-gray-800 leading-relaxed`}
        >
          {text}
        </div>
        <span className="text-xs text-gray-500 mt-1">{time}</span>
      </div>
    </div>
  );
}